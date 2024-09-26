package findo.schedule.core;

import lombok.Getter;

@Getter
public enum AppConstant {
    ScheduleErrorCreateMsg("Error while creating schedule");

    private String value;

    private AppConstant(String value) {
        this.value = value;
    }
}
