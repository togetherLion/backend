package CodeMaker.togetherLion.domain.place.model;

import lombok.Getter;

@Getter
public enum PlaceType {

    STORE("편의점"),
    POLICE("경찰서"),
    STATION("대중교통"),
    OTHER("기타");

    private final String message;

    PlaceType(String message) {
        this.message = message;
    }
}
