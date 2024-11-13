package com.iot.iot_BE.service;

import com.iot.iot_BE.model.CountWarning;
import com.iot.iot_BE.repository.CountWarningRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import org.springframework.scheduling.annotation.Scheduled;

@Service
public class CountWarningService {
    private static final Logger logger = LoggerFactory.getLogger(CountWarningService.class);

    @Autowired
    private CountWarningRepository countWarningRepository;

    public void updateDustCount(long dustCount) {
        CountWarning countWarning = countWarningRepository.findById(1L).orElse(new CountWarning());
        countWarning.setCount(dustCount);
        countWarningRepository.save(countWarning);
        logger.info("Updated count warning to: {}", dustCount);
    }

    // Tạo tác vụ lặp lại hàng giờ
    @Scheduled(fixedRate = 3600000) //86.400.000=ngày// Lặp lại sau mỗi giờ (3600000 ms)
    public void resetDustCountHourly() {
        CountWarning countWarning = countWarningRepository.findById(1L).orElse(new CountWarning());
        countWarning.setCount(0L); // Reset giá trị count về 0
        countWarningRepository.save(countWarning);
        logger.info("Reset count warning to 0.");
    }

    public long getDustCount() {
        return countWarningRepository.findById(1L)
                .map(CountWarning::getCount)
                .orElse(0L); // trả về 0 nếu không tìm thấy bản ghi
    }

}
