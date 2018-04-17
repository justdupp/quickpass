package hecc.pay.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author xuhoujun
 * @description: 分页util
 * @date: Created In 下午9:49 on 2018/3/19.
 */
public class PageUtil {

    private static final String SORT_TYPE = "createDate";

    public static Pageable generatePage(Integer page) {
        return new PageRequest(page == null ? 0 : page - 1, 10, new Sort(Sort.Direction.DESC, SORT_TYPE));
    }
}
