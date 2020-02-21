/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supportMethods;

/**
 *
 * @author hoang
 */
public class PagingHandler {

    public PagingHandler() {
    }

    private boolean isInteger(String number) {
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public int getPage(String pg) {
        int page = 0;

        if (isInteger(pg) || pg == null) {
            page = 1;
            if (pg != null) {
                page = Integer.parseInt(pg);
            }
        }

        return page;
    }

    public int getTotalPage(String pg, int blogsTotal, int numOfBlogsPerPage) {
        int totalPage;

        double tmp = (double) blogsTotal / numOfBlogsPerPage;
        if (tmp == (int) tmp) {
            totalPage = (int) tmp;
        } else {
            totalPage = (int) tmp + 1;
        }

        return totalPage;
    }
}
