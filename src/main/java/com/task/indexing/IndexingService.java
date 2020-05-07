package com.task.indexing;

import com.task.tools.UrlTester;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
public class IndexingService {
    private final IndexingRepository indexingRepository;

    public IndexingService(IndexingRepository indexingRepository) {
        this.indexingRepository = indexingRepository;
    }

    public boolean saveLinkToDatabase(String url) {
        if (indexingRepository.checkLinkForAvailabilityInDatabase(url)) {
            indexingRepository.save(url);
            return true;
        }
        return false;
    }
}
