package com.chipchippoker.backend.common.exception;

import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.dto.MessageBase;

import lombok.Getter;

@Getter
public abstract class ChipChipPokerBaseException extends RuntimeException {
	private final ErrorBase errorBase;
	private final MessageBase messageBase;

	protected ChipChipPokerBaseException(ErrorBase errorBase) {
		super(errorBase.getMessage());
		this.errorBase = errorBase;
		this.messageBase = null;
	}

	protected ChipChipPokerBaseException(MessageBase messageBase) {
		super(messageBase.getMessage());
		this.errorBase = null;
		this.messageBase = messageBase;
	}
}