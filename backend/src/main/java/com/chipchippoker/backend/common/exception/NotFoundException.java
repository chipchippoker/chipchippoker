package com.chipchippoker.backend.common.exception;

import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.dto.MessageBase;

public class NotFoundException extends ChipChipPokerBaseException {

	public NotFoundException(ErrorBase errorBase) {
		super(errorBase);
	}

	public NotFoundException(MessageBase messageBase) {
		super(messageBase);
	}
}