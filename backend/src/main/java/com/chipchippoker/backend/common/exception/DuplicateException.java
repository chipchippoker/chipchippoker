package com.chipchippoker.backend.common.exception;

import com.chipchippoker.backend.common.dto.ErrorBase;

public class DuplicateException extends ChipChipPokerBaseException {
    public DuplicateException(ErrorBase errorBase) {
        super(errorBase);
    }
}
