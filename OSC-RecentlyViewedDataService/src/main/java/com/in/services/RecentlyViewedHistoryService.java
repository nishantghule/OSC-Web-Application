package com.in.services;

import com.in.dtos.RecentlyViewedProductDTO;
import com.in.dtos.UpdateRecentlyViewedDTO;
import java.util.*;
public interface RecentlyViewedHistoryService {
    Boolean updateRecentlyViewedProducts(UpdateRecentlyViewedDTO request);
    List<String> fetchRecentlyViewedProducts(String userId);
    void saveRecentlyViewedProducts(RecentlyViewedProductDTO dto);
}
