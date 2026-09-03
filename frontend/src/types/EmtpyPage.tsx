import type PageResponse from "./PageResponse"
import type { PageRequest } from "./PageRequest";

export function emptyPage<T>(requestQueryParams: PageRequest): PageResponse<T> {
    return {
        content: [],
        totalPages: 0,
        totalElements: 0,
        page: requestQueryParams.page,
        size: requestQueryParams.size,
        sort: requestQueryParams.sort
    };
}
