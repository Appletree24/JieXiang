package com.sky31.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @AUTHOR Zzh
 * @DATE 2022/8/2
 * @TIME 14:25
 */
@Data
@Getter
@Setter
public class Page {
    private int current = 1;
    private int limit = 10;
    private int rows;
    private String path;

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public int getOffset() {
        //current*limit-limit
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     *
     * @return
     */
    public int getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    public int getFrom(){
        int from=current-2;
        return Math.max(from, 1);
    }

    public int getTo(){
        int to=current+2;
        int total=getTotal();
        return Math.min(to, total);
    }
}
