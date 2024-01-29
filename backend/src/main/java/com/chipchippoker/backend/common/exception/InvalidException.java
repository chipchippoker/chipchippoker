package com.chipchippoker.backend.common.exception;

import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.dto.MessageBase;

public class InvalidException extends ChipChipPokerBaseException {

	public InvalidException(ErrorBase errorBase) {
		super(errorBase);
	}

	public InvalidException(MessageBase messageBase) {
		super(messageBase);
	}
}