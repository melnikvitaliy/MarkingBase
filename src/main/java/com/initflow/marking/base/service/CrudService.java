package com.initflow.marking.base.service;

import com.initflow.marking.base.models.SearchRequest;
import com.initflow.marking.base.models.domain.IDObj;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface CrudService<T extends IDObj<ID>, ID extends Serializable>  extends ReadAndSaveService<T,ID>{

    <K> T create(K dto, Function<K, T> mapper);
    <K> T update(ID id, K dto, BiConsumer<K, T> mapper);
    <K> K read(ID id, Function<T, K> mapper);
    <K> Page<K> readList(List<ID> ids, Pageable pageable, Function<T, K> mapper);
    Page<T> findByids(List<ID> ids, Pageable pageable);
    <SR extends SearchRequest, K> Page<K> searchAndMap(Pageable pageable, SR searchRequest, Function<T, K> mapper);
    <SR extends SearchRequest> Page<T> findAll(Pageable pageable, SR searchRequest);
}
