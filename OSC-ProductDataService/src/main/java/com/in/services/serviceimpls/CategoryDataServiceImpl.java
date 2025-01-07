package com.in.services.serviceimpls;

import com.in.dtos.CategoryDetailsDTO;
import com.in.entities.CategoryEntity;
import com.in.repositories.CategoryDataRepository;
import com.in.services.CategoryDataService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryDataServiceImpl implements CategoryDataService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryDataServiceImpl.class);

    private final CategoryDataRepository categoryDataRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoryDetailsDTO> getAllCategories() {
        logger.info("Fetching all categories from the repository.");

        List<CategoryEntity> categoryEntities = categoryDataRepository.findAll();
        if (categoryEntities.isEmpty()) {
            logger.warn("No categories found in the database.");
        } else {
            logger.info("Successfully fetched {} categories from the repository.", categoryEntities.size());
        }

        List<CategoryDetailsDTO> categoryDetailsDTOList = categoryEntities.stream()
                .map(categoryEntity -> {
                    CategoryDetailsDTO categoryDetailsDTO = modelMapper.map(categoryEntity, CategoryDetailsDTO.class);
                    logger.debug("Mapping CategoryEntity with ID: {} to CategoryDetailsDTO.", categoryEntity.getCategoryId());
                    return categoryDetailsDTO;
                })
                .toList();

        logger.info("Mapped all {} CategoryEntities to CategoryDetailsDTOs.", categoryDetailsDTOList.size());
        return categoryDetailsDTOList;
    }
}
