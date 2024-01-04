package com.bootdo.modular.system.domain;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author L
 */
@Data
public class RoleDO {
	
	private Long roleId;
	private String roleName;
	private String roleSign;
	private String remark;
	private Long userIdCreate;
	private Timestamp gmtCreate;
	private Timestamp gmtModified;
	private List<Long> menuIds;

}
