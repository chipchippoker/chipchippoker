package com.chipchippoker.backend.websocket.spectation.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
public class SpectationManager {
	private String roomTitle;
	private List<String> spectatorList;

	public SpectationManager(String roomTitle) {
		log.info("관전방 매니저를 생성합니다.");
		this.roomTitle = roomTitle;
		this.spectatorList = new ArrayList<>();
		log.info("관전방 매니저를 생성했습니다.");
	}

	public void insertMember(String nickname) {
		spectatorList.add(nickname);
	}

	public void deleteMember(String nickname) {
		spectatorList.remove(nickname);
	}
}
