package com.CharityBaptistChurch.CharityBible.Items;

public class ListViewItem {
    private String m_strChapter;      // 장    ( 1 장 )
    private String m_strSection;      // 절    ( 20절 )
    private String m_strVerse;        // 절 텍스트 내용 ( 태초에 ---- )
    private int m_nReplaceNumber;     // 성경 대조 번호

    public ListViewItem(String strChapter, String strSection, String strVerse, int nReplaceNumber)
    {
        this.m_strChapter = strChapter;
        this.m_strSection = strSection;
        this.m_strVerse = strVerse;
        this.m_nReplaceNumber = nReplaceNumber;
    }

    // Getter
    public String getStrChapter() {
        return m_strChapter;
    }

    public String getStrSection() {
        return m_strSection;
    }

    public String getStrVerse() { return m_strVerse; }

    public int getReplaceNumber() { return m_nReplaceNumber; }


    // Setter
    public void setReplaceNumber(int m_nReplaceNumber) { this.m_nReplaceNumber = m_nReplaceNumber;   }

    public void setStrChapter(String strChapter) {
        this.m_strChapter = strChapter;
    }

    public void setStrSection(String strSection) {
        this.m_strSection = strSection;
    }

    public void setStrVerse(String strVerse) { this.m_strVerse = strVerse; }




}
