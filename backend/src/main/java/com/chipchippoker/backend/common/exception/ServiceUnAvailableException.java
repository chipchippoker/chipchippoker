package com.chipchippoker.backend.common.exception;

import com.chipchippoker.backend.common.dto.ErrorBase;

public class ServiceUnAvailableException extends ChipChipPokerBaseException {
    public ServiceUnAvailableException(ErrorBase errorBase) {
        super(errorBase);
    }
}
