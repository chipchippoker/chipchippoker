package com.chipchippoker.backend.common.exception;

import com.chipchippoker.backend.common.dto.ErrorBase;
import lombok.Getter;

@Getter
public abstract class ChipChipPokerBaseException extends RuntimeException{
    private final ErrorBase errorBase;

    protected ChipChipPokerBaseException(ErrorBase errorBase) {
        super(errorBase.getMessage());
        this.errorBase = errorBase;
    }
}