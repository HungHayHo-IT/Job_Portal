package com.example.BE.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateActivityService {

    private final RedisTemplate<String, Object> redisTemplate;

    public List<Object> getRecentViewedJobs(String userEmail) {
        String key = "recent_jobs:" + userEmail;
        // Lấy từ phần tử 0 đến phần tử cuối cùng (-1)
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    // Sử dụng SET: Đánh dấu công việc đã ứng tuyển (Unique)[cite: 2]
    public void markJobAsApplied(String userEmail, Long jobId) {
        String key = "applied_jobs:" + userEmail;
        // Thêm jobId vào Set[cite: 2]
        redisTemplate.opsForSet().add(key, jobId.toString());
    }

    // Kiểm tra xem ứng viên đã nộp đơn chưa
    public boolean hasAppliedForJob(String userEmail, Long jobId) {
        String key = "applied_jobs:" + userEmail;
        // Kiểm tra phần tử có tồn tại trong Set không (trả về true/false)[cite: 2]
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, jobId.toString()));
    }

    public void incrementJobView(Long jobId) {
        String key = "job_views:" + jobId;
        // Tăng giá trị lên 1 (nếu key chưa có, Redis tự tạo và gán bằng 1)[cite: 2]
        redisTemplate.opsForValue().increment(key);
    }

    public Integer getJobViews(Long jobId) {
        String key = "job_views:" + jobId;
        Object views = redisTemplate.opsForValue().get(key);
        return views != null ? Integer.parseInt(views.toString()) : 0;
    }

    // Sử dụng HASH: Cập nhật từng field riêng lẻ[cite: 2]
    public void updateCompanyStat(Long companyId, String field, String value) {
        String key = "company_stats:" + companyId;
        // Cập nhật 1 trường dữ liệu cụ thể trong Hash[cite: 2]
        redisTemplate.opsForHash().put(key, field, value);
    }

    public Object getCompanyStat(Long companyId, String field) {
        String key = "company_stats:" + companyId;
        return redisTemplate.opsForHash().get(key, field);
    }
}
