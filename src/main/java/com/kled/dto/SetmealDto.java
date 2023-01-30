package com.kled.dto;


import com.kled.common.R;
import com.kled.domain.Setmeal;
import com.kled.domain.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;

    public R<String> save(SetmealDto setmealDto){

        return null;
    }


}
