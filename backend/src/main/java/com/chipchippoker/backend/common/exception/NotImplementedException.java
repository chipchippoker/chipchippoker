package com.chipchippoker.backend.common.exception;

import com.chipchippoker.backend.common.dto.ErrorBase;

public class NotImplementedException extends ChipChipPokerBaseException {
    public NotImplementedException(ErrorBase errorBase) {
        super(errorBase);
    }

}