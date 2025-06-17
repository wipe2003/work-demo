package com.wipe.loggingservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wipe.loggingservice.pojo.domain.OperationLogs;
import com.wipe.loggingservice.service.OperationLogsService;
import com.wipe.loggingservice.mapper.OperationLogsMapper;
import org.springframework.stereotype.Service;

/**
* @author 29770
* @description 针对表【operation_logs】的数据库操作Service实现
* @createDate 2025-06-16 16:40:20
*/
@Service
public class OperationLogsServiceImpl extends ServiceImpl<OperationLogsMapper, OperationLogs>
    implements OperationLogsService {

}




