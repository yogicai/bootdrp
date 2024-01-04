package com.bootdo.modular.system.domain;

import lombok.Data;

/**
 * @author L
 */
@Data
public class NotifyDTO extends NotifyDO {

    private String isRead;

    private String before;

    private String sender;

}
