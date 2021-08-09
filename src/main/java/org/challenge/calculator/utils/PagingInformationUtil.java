package org.challenge.calculator.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * This class helps with the building of Pageable objects.
 * Pageable objects are used by repositories to know which and
 * how much information should return.
 */
public class PagingInformationUtil {

    public static Pageable buildPagingInformation(int pageIndex, int pageSize, String sortingField){
        Pageable pagingInformation;
        if (StringUtils.isNotBlank(sortingField)) {
            pagingInformation = PageRequest.of(pageIndex, pageSize, Sort.by(sortingField));
        } else {
            pagingInformation = PageRequest.of(pageIndex, pageSize);
        }
        return pagingInformation;
    }
}
