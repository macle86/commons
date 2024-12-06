/*
 * Copyright (C) 2020 Seomse Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.runon.commons.utils.string.highlight;

import io.runon.commons.data.BeginEnd;
import io.runon.commons.exception.OutOfRangeException;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 문자열 하이라이트
 * @author macle
 */
public class StringHighlight {

    /**
     * 토큰 중복위치에 따른 처리가 필요함
     * Highlight 문자열 생성
     * @param text String
     * @param tokens String []
     * @param tokenIndexes int []
     * @param splitBeginEnds BeginEnd []
     * @param keywords String []
     * @param pre String
     * @param post String
     * @param length int
     * @return String
     */
    public static String make(String text, String [] tokens, int [] tokenIndexes, BeginEnd[] splitBeginEnds, String [] keywords, String pre, String post, int length ){

        //글자 길이수 역순으로 정렬
        Arrays.sort(keywords, (a, b)->Integer.compare(b.length(), a.length()));


        HighlightSearch max = null;
        HighlightSearch check =  null;
        outer:
        for (int i = 0; i <tokens.length ; i++) {
            String token = tokens[i];
            for (int jj = 0; jj <keywords.length ; jj++) {
                String keyword = keywords[jj];

                if(
                        token.equals(keyword)
                                || ( keyword.length() > 1 && token.startsWith(keyword) )
                                || ( keyword.length() > 1 && token.endsWith(keyword) )
                ){

                    HighlightKeyword highlightKeyword = new HighlightKeyword();
                    highlightKeyword.index = jj;
                    highlightKeyword.begin = tokenIndexes[i];
                    highlightKeyword.end = tokenIndexes[i]+keyword.length();



                    if(check == null
                            ||  highlightKeyword.end - check.list.get(0).begin > length
                    ){
                        HighlightSearch search = new HighlightSearch();
                        search.list.add(highlightKeyword);
                        search.keywordIndexSet.add(jj);

                        check = search;

                        if(max == null){
                            max = search;
                        }

                    }else{
                        //추가인지 교체인지 체크하기
                        //--겹쳐있으면 시작위치가 같으면 큰걸로 교체하고, 시작위치가 다른 겹친범위는 무시한다. (경우가 발생하지 않음)
                        //글자수로 정렬되어 있기때문에 겹친 범위는 전무 무시하게 한다.

                        if(check.isOverlap(highlightKeyword.getBegin(), highlightKeyword.getEnd())){
                            continue ;
                        }

                        check.list.add(highlightKeyword);
                        check.keywordIndexSet.add(jj);

                        if(
                                max.keywordIndexSet.size() < check.keywordIndexSet.size()
                                        || (max.keywordIndexSet.size() == check.keywordIndexSet.size() && max.list.size() < check.list.size())
                        ){
                            max = check;
                        }

                    }

                    if(max.keywordIndexSet.size() == keywords.length){
                        break outer;
                    }



                    break;
                }
            }
        }


        if(max == null){
            if(length > text.length()){
                return text;
            }else{
                return text.substring(0, length);
            }
        }

        int highlightBegin;
        int highlightEnd;

        if(text.length() < length){
            highlightBegin = 0;
            highlightEnd = text.length();
        }else{
            highlightBegin = max.list.get(0).begin;
            highlightEnd = max.list.get(max.list.size()-1).end;

            int gap = length - (highlightEnd - highlightBegin);


            //범위확장 가능여부 체크
            int splitBegin = -1;

            for (BeginEnd beginEnd : splitBeginEnds) {
                if (beginEnd.getBegin() <= highlightBegin && beginEnd.getEnd() > highlightBegin) {
                    splitBegin = beginEnd.getBegin();
                    break;
                }
            }

            if(splitBegin == -1){
                throw new OutOfRangeException("splitBeginEnds error");
            }

            //앞부분 확장
            if( highlightEnd - splitBegin < length){
                highlightBegin = splitBegin;

            }else{

                int spaceIndex = text.lastIndexOf(' ', highlightBegin);
                if(spaceIndex == -1){
                    if(highlightBegin < gap){
                        highlightBegin = 0;
                    }
                }else{

                    int checkIndex = spaceIndex+1;

                    if(checkIndex != highlightBegin && highlightBegin - checkIndex < gap){
                        highlightBegin = checkIndex;
                    }
                }
            }

            //뒷 부분 확장
            gap = length - (highlightEnd - highlightBegin);

            highlightEnd = Math.min(highlightEnd + gap, text.length());
        }

        HighlightKeyword [] highlightKeywords = max.list.toArray(new HighlightKeyword[0]);
        for(HighlightKeyword highlightKeyword : highlightKeywords){
            highlightKeyword.begin -= highlightBegin;
            highlightKeyword.end -= highlightBegin;
        }



        if(highlightEnd == text.length()){
            return make (text.substring(highlightBegin, highlightEnd), pre, post, highlightKeywords, false);
        }else{
            return make (text.substring(highlightBegin, highlightEnd), pre, post, highlightKeywords, false) + "...";
        }
    }


    /***
     * Highlight 문자열 생성
     * @param text String
     * @param pre String
     * @param post String
     * @param beginEnds BeginEnd []
     * @return String
     */
    public static String make(String text, String pre, String post, BeginEnd[] beginEnds) {
        return make(text, pre, post, beginEnds, true);
    }


    /***
     * Highlight 문자열 생성
     * @param text String
     * @param pre String
     * @param post String
     * @param beginEnds BeginEnd []
     * @param isSort boolean beginEnds sort
     * @return String
     */
    public static String make(String text, String pre, String post, BeginEnd[] beginEnds, boolean isSort){

        if(isSort) {
            Arrays.sort(beginEnds, Comparator.comparingInt(BeginEnd::getBegin));
        }
        StringBuilder sb = new StringBuilder();
        int lastIndex = 0;

        for(BeginEnd beginEnd : beginEnds){
            sb.append(text, lastIndex, beginEnd.getBegin());
            sb.append(pre).append(text, beginEnd.getBegin(), beginEnd.getEnd()).append(post);
            lastIndex = beginEnd.getEnd();
        }

        if(lastIndex != text.length()){
            sb.append(text, lastIndex, text.length());
        }

        return sb.toString();
    }
}