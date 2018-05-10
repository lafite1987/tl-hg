package cn.lfy.base.dto;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
public class CampusPlanDTO {

	@NotEmpty(message = "{position.name.null}")  
    @Length(min = 2, max = 5, message = "{position.name.length}")  
	private String positionName;  
	@Valid
	private Object obj;  
}
