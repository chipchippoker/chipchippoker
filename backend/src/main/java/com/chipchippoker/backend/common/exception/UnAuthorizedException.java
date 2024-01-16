package com.chipchippoker.backend.common.exception;

import com.chipchippoker.backend.common.dto.ErrorBase;

public class UnAuthorizedException extends ChipChipPokerBaseException {

	public UnAuthorizedException(ErrorBase errorBase) {
		super(errorBase);
	}

	public UnAuthorizedException(ErrorBase errorBase, String message) {
		super(errorBase);
	}
}