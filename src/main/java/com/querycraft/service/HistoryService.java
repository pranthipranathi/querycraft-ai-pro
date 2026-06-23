package com.querycraft.service;

import com.querycraft.entity.QueryHistory;
import com.querycraft.entity.User;
import com.querycraft.exception.ResourceNotFoundException;
import com.querycraft.repository.QueryHistoryRepository;
import com.querycraft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private QueryHistoryRepository queryHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    public QueryHistory saveHistory(String username,
                                    String naturalQuery,
                                    String generatedSql,
                                    String explanation,
                                    QueryHistory.ExecutionStatus status,
                                    Integer rowCount,
                                    Long executionTimeMs,
                                    String errorMessage) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + username));

        QueryHistory history = new QueryHistory();
        history.setUser(user);
        history.setNaturalLanguageQuery(naturalQuery);
        history.setGeneratedSql(generatedSql);
        history.setExplanation(explanation);
        history.setExecutionStatus(status);
        history.setResultRowCount(rowCount);
        history.setExecutionTimeMs(executionTimeMs);
        history.setErrorMessage(errorMessage);

        return queryHistoryRepository.save(history);
    }

    public List<QueryHistory> getUserHistory(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + username));
        return queryHistoryRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public void deleteHistory(Long historyId) {
        queryHistoryRepository.deleteById(historyId);
    }
}