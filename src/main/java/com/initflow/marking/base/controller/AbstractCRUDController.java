package com.initflow.marking.base.controller;

import com.initflow.marking.base.mapper.domain.CrudMapper;
import com.initflow.marking.base.models.SearchRequest;
import com.initflow.marking.base.models.domain.IDObj;
import com.initflow.marking.base.service.CrudService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class AbstractCRUDController<T extends IDObj<ID>, C_DTO, U_DTO, R_DTO, ID extends Serializable, SR extends SearchRequest> {

    private CrudService<T, ID> crudService;
    private CrudMapper<T, C_DTO, U_DTO, R_DTO> mapper;
//    private CRUDPermission permission;

    public AbstractCRUDController(CrudService<T, ID> crudService, CrudMapper<T, C_DTO, U_DTO, R_DTO> mapper){
        this.crudService = crudService;
        this.mapper = mapper;
//        this.permission = new DefaultCRUDPermissionImpl();
    }

    @PreAuthorize("hasAnyAuthority(#root.this.getReadRoles()) and #root.this.getReadPerm(#id, #request, #header)")
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "read_list", notes = "read_list")
    public @ResponseBody
    Page<R_DTO> readList(@RequestParam(value = "ids", required = false) List<ID> ids, Pageable pageable, HttpServletRequest request, @RequestHeader Map<String, String> header) {
        return crudService.readList(ids, pageable, getReadMapper());
//        R_DTO dto = crudService.read(id, getReadMapper());
//        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyAuthority(#root.this.getReadRoles()) and #root.this.getSearchPerm(#searchRequest, #request, #header)")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "read_list search", notes = "read_list search")
    public @ResponseBody
    Page<R_DTO> readList(Pageable pageable, SR searchRequest,
                         HttpServletRequest request, @RequestHeader Map<String, String> header) {
        return crudService.searchAndMap(pageable, searchRequest, getReadMapper());
//        R_DTO dto = crudService.read(id, getReadMapper());
//        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyAuthority(#root.this.getReadRoles()) and #root.this.getReadPerm(#id, #request, #header)")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "read", notes = "read")
    public @ResponseBody
    ResponseEntity<R_DTO> read(@PathVariable ID id,
                               HttpServletRequest request, @RequestHeader Map<String, String> header) {
        R_DTO dto = crudService.read(id, getReadMapper());
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyAuthority(#root.this.updateRoles) and #root.this.getUpdatePerm(#id, #dto, #request, #header)")
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @ApiOperation(value = "update", notes = "update")
    public @ResponseBody
    ResponseEntity<R_DTO> update(@PathVariable ID id, @RequestBody U_DTO dto,
                                 HttpServletRequest request, @RequestHeader Map<String, String> header) {
        T obj = crudService.update(id, dto, getUpdateMapper());
        R_DTO respDTO = getReadMapper().apply(obj);
        postCreateFunc(obj);
        return ResponseEntity.ok(respDTO);
    }

    @PreAuthorize("hasAnyAuthority(#root.this.createRoles) and #root.this.getCreatePerm(#dto, #request, #header)")
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "create", notes = "Create")
    public @ResponseBody
    ResponseEntity<R_DTO> create(@RequestBody C_DTO dto,
                                 HttpServletRequest request, @RequestHeader Map<String, String> header) {
        T obj = crudService.create(dto, getCreateMapper());
        R_DTO respDTO = getReadMapper().apply(obj);
        postCreateFunc(obj);
        return ResponseEntity.ok(respDTO);
    }

    protected Function<T, R_DTO> getReadMapper() {
        return (T obj) -> mapper.readMapping(obj);
    }

    protected BiConsumer<U_DTO, T> getUpdateMapper() {
        return (U_DTO from, T to) -> mapper.updateMapping(from, to);
    }

    protected Function<C_DTO, T> getCreateMapper() {
        return (C_DTO dto) -> mapper.createMapping(dto);
    }

    public abstract String[] getReadRoles();

    public abstract String[] getUpdateRoles();

    public abstract String[] getCreateRoles();

    public boolean getReadPerm(ID id, HttpServletRequest request, Map<String, String> header){
        return true;
    }

    public boolean getSearchPerm(SR searchRequest, HttpServletRequest request, Map<String, String> header){
        return true;
    }

    public boolean getUpdatePerm(ID id, U_DTO dto, HttpServletRequest request, Map<String, String> header){
        return true;
    }

    public boolean getCreatePerm(C_DTO dto, HttpServletRequest request, Map<String, String> header){
        return true;
    }

    public void postCreateFunc(T obj){}
//    public void postReadFunc(T obj){}
    public void postUpdateFunc(T obj){}

}