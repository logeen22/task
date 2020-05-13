package com.task.indexing;

import org.springframework.stereotype.Service;

@Service
public class IndexingService {
    private final IndexingRepository indexingRepository;

    public IndexingService(IndexingRepository indexingRepository) {
        this.indexingRepository = indexingRepository;
    }

    public boolean saveLinkToDatabase(String link) {
        if (indexingRepository.isLinkAvailableInDatabase(link)) {
            indexingRepository.save(link);
            return true;
        }
        return false;
    }
}
