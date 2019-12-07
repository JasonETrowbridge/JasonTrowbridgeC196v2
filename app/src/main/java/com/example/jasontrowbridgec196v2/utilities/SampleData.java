package com.example.jasontrowbridgec196v2.utilities;

import com.example.jasontrowbridgec196v2.Database.TermEntity;

import java.util.ArrayList;
import java.util.List;

public class SampleData {
    private static final String TITLE_TERM_TEXT_1 = "Term 1";
    private static final String TITLE_TERM_TEXT_2 = "Term 2";
    private static final String TITLE_TERM_TEXT_3 = "Term 3";

    private static final String DATE_TEXT_1 = "2019-04-01";
    private static final String DATE_TEXT_2 = "2019-11-01";
    private static final String DATE_TEXT_3 = "2020-04-01";
    private static final String DATE_TEXT_4 = "2020-11-01";

    public static List<TermEntity> getTerms() {
        List<TermEntity> terms = new ArrayList<>();
        terms.add(new TermEntity(DATE_TEXT_1, DATE_TEXT_2, TITLE_TERM_TEXT_1));
        terms.add(new TermEntity(DATE_TEXT_2, DATE_TEXT_3, TITLE_TERM_TEXT_2));
        terms.add(new TermEntity(DATE_TEXT_3, DATE_TEXT_4, TITLE_TERM_TEXT_3));
        return terms;
    }

}
