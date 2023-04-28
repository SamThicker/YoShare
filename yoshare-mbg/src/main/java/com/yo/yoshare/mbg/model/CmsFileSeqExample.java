package com.yo.yoshare.mbg.model;

import java.util.ArrayList;
import java.util.List;

public class CmsFileSeqExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CmsFileSeqExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("`name` is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("`name` is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("`name` =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("`name` <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("`name` >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("`name` >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("`name` <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("`name` <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("`name` like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("`name` not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("`name` in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("`name` not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("`name` between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("`name` not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andPartsNumIsNull() {
            addCriterion("parts_num is null");
            return (Criteria) this;
        }

        public Criteria andPartsNumIsNotNull() {
            addCriterion("parts_num is not null");
            return (Criteria) this;
        }

        public Criteria andPartsNumEqualTo(Integer value) {
            addCriterion("parts_num =", value, "partsNum");
            return (Criteria) this;
        }

        public Criteria andPartsNumNotEqualTo(Integer value) {
            addCriterion("parts_num <>", value, "partsNum");
            return (Criteria) this;
        }

        public Criteria andPartsNumGreaterThan(Integer value) {
            addCriterion("parts_num >", value, "partsNum");
            return (Criteria) this;
        }

        public Criteria andPartsNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("parts_num >=", value, "partsNum");
            return (Criteria) this;
        }

        public Criteria andPartsNumLessThan(Integer value) {
            addCriterion("parts_num <", value, "partsNum");
            return (Criteria) this;
        }

        public Criteria andPartsNumLessThanOrEqualTo(Integer value) {
            addCriterion("parts_num <=", value, "partsNum");
            return (Criteria) this;
        }

        public Criteria andPartsNumIn(List<Integer> values) {
            addCriterion("parts_num in", values, "partsNum");
            return (Criteria) this;
        }

        public Criteria andPartsNumNotIn(List<Integer> values) {
            addCriterion("parts_num not in", values, "partsNum");
            return (Criteria) this;
        }

        public Criteria andPartsNumBetween(Integer value1, Integer value2) {
            addCriterion("parts_num between", value1, value2, "partsNum");
            return (Criteria) this;
        }

        public Criteria andPartsNumNotBetween(Integer value1, Integer value2) {
            addCriterion("parts_num not between", value1, value2, "partsNum");
            return (Criteria) this;
        }

        public Criteria andPartSeqIsNull() {
            addCriterion("part_seq is null");
            return (Criteria) this;
        }

        public Criteria andPartSeqIsNotNull() {
            addCriterion("part_seq is not null");
            return (Criteria) this;
        }

        public Criteria andPartSeqEqualTo(Integer value) {
            addCriterion("part_seq =", value, "partSeq");
            return (Criteria) this;
        }

        public Criteria andPartSeqNotEqualTo(Integer value) {
            addCriterion("part_seq <>", value, "partSeq");
            return (Criteria) this;
        }

        public Criteria andPartSeqGreaterThan(Integer value) {
            addCriterion("part_seq >", value, "partSeq");
            return (Criteria) this;
        }

        public Criteria andPartSeqGreaterThanOrEqualTo(Integer value) {
            addCriterion("part_seq >=", value, "partSeq");
            return (Criteria) this;
        }

        public Criteria andPartSeqLessThan(Integer value) {
            addCriterion("part_seq <", value, "partSeq");
            return (Criteria) this;
        }

        public Criteria andPartSeqLessThanOrEqualTo(Integer value) {
            addCriterion("part_seq <=", value, "partSeq");
            return (Criteria) this;
        }

        public Criteria andPartSeqIn(List<Integer> values) {
            addCriterion("part_seq in", values, "partSeq");
            return (Criteria) this;
        }

        public Criteria andPartSeqNotIn(List<Integer> values) {
            addCriterion("part_seq not in", values, "partSeq");
            return (Criteria) this;
        }

        public Criteria andPartSeqBetween(Integer value1, Integer value2) {
            addCriterion("part_seq between", value1, value2, "partSeq");
            return (Criteria) this;
        }

        public Criteria andPartSeqNotBetween(Integer value1, Integer value2) {
            addCriterion("part_seq not between", value1, value2, "partSeq");
            return (Criteria) this;
        }

        public Criteria andSizeIsNull() {
            addCriterion("`size` is null");
            return (Criteria) this;
        }

        public Criteria andSizeIsNotNull() {
            addCriterion("`size` is not null");
            return (Criteria) this;
        }

        public Criteria andSizeEqualTo(Long value) {
            addCriterion("`size` =", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotEqualTo(Long value) {
            addCriterion("`size` <>", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThan(Long value) {
            addCriterion("`size` >", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThanOrEqualTo(Long value) {
            addCriterion("`size` >=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThan(Long value) {
            addCriterion("`size` <", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThanOrEqualTo(Long value) {
            addCriterion("`size` <=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeIn(List<Long> values) {
            addCriterion("`size` in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotIn(List<Long> values) {
            addCriterion("`size` not in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeBetween(Long value1, Long value2) {
            addCriterion("`size` between", value1, value2, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotBetween(Long value1, Long value2) {
            addCriterion("`size` not between", value1, value2, "size");
            return (Criteria) this;
        }

        public Criteria andTotalSizeIsNull() {
            addCriterion("total_size is null");
            return (Criteria) this;
        }

        public Criteria andTotalSizeIsNotNull() {
            addCriterion("total_size is not null");
            return (Criteria) this;
        }

        public Criteria andTotalSizeEqualTo(Long value) {
            addCriterion("total_size =", value, "totalSize");
            return (Criteria) this;
        }

        public Criteria andTotalSizeNotEqualTo(Long value) {
            addCriterion("total_size <>", value, "totalSize");
            return (Criteria) this;
        }

        public Criteria andTotalSizeGreaterThan(Long value) {
            addCriterion("total_size >", value, "totalSize");
            return (Criteria) this;
        }

        public Criteria andTotalSizeGreaterThanOrEqualTo(Long value) {
            addCriterion("total_size >=", value, "totalSize");
            return (Criteria) this;
        }

        public Criteria andTotalSizeLessThan(Long value) {
            addCriterion("total_size <", value, "totalSize");
            return (Criteria) this;
        }

        public Criteria andTotalSizeLessThanOrEqualTo(Long value) {
            addCriterion("total_size <=", value, "totalSize");
            return (Criteria) this;
        }

        public Criteria andTotalSizeIn(List<Long> values) {
            addCriterion("total_size in", values, "totalSize");
            return (Criteria) this;
        }

        public Criteria andTotalSizeNotIn(List<Long> values) {
            addCriterion("total_size not in", values, "totalSize");
            return (Criteria) this;
        }

        public Criteria andTotalSizeBetween(Long value1, Long value2) {
            addCriterion("total_size between", value1, value2, "totalSize");
            return (Criteria) this;
        }

        public Criteria andTotalSizeNotBetween(Long value1, Long value2) {
            addCriterion("total_size not between", value1, value2, "totalSize");
            return (Criteria) this;
        }

        public Criteria andHasMoreIsNull() {
            addCriterion("has_more is null");
            return (Criteria) this;
        }

        public Criteria andHasMoreIsNotNull() {
            addCriterion("has_more is not null");
            return (Criteria) this;
        }

        public Criteria andHasMoreEqualTo(String value) {
            addCriterion("has_more =", value, "hasMore");
            return (Criteria) this;
        }

        public Criteria andHasMoreNotEqualTo(String value) {
            addCriterion("has_more <>", value, "hasMore");
            return (Criteria) this;
        }

        public Criteria andHasMoreGreaterThan(String value) {
            addCriterion("has_more >", value, "hasMore");
            return (Criteria) this;
        }

        public Criteria andHasMoreGreaterThanOrEqualTo(String value) {
            addCriterion("has_more >=", value, "hasMore");
            return (Criteria) this;
        }

        public Criteria andHasMoreLessThan(String value) {
            addCriterion("has_more <", value, "hasMore");
            return (Criteria) this;
        }

        public Criteria andHasMoreLessThanOrEqualTo(String value) {
            addCriterion("has_more <=", value, "hasMore");
            return (Criteria) this;
        }

        public Criteria andHasMoreLike(String value) {
            addCriterion("has_more like", value, "hasMore");
            return (Criteria) this;
        }

        public Criteria andHasMoreNotLike(String value) {
            addCriterion("has_more not like", value, "hasMore");
            return (Criteria) this;
        }

        public Criteria andHasMoreIn(List<String> values) {
            addCriterion("has_more in", values, "hasMore");
            return (Criteria) this;
        }

        public Criteria andHasMoreNotIn(List<String> values) {
            addCriterion("has_more not in", values, "hasMore");
            return (Criteria) this;
        }

        public Criteria andHasMoreBetween(String value1, String value2) {
            addCriterion("has_more between", value1, value2, "hasMore");
            return (Criteria) this;
        }

        public Criteria andHasMoreNotBetween(String value1, String value2) {
            addCriterion("has_more not between", value1, value2, "hasMore");
            return (Criteria) this;
        }

        public Criteria andMemberIdIsNull() {
            addCriterion("member_id is null");
            return (Criteria) this;
        }

        public Criteria andMemberIdIsNotNull() {
            addCriterion("member_id is not null");
            return (Criteria) this;
        }

        public Criteria andMemberIdEqualTo(Long value) {
            addCriterion("member_id =", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdNotEqualTo(Long value) {
            addCriterion("member_id <>", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdGreaterThan(Long value) {
            addCriterion("member_id >", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdGreaterThanOrEqualTo(Long value) {
            addCriterion("member_id >=", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdLessThan(Long value) {
            addCriterion("member_id <", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdLessThanOrEqualTo(Long value) {
            addCriterion("member_id <=", value, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdIn(List<Long> values) {
            addCriterion("member_id in", values, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdNotIn(List<Long> values) {
            addCriterion("member_id not in", values, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdBetween(Long value1, Long value2) {
            addCriterion("member_id between", value1, value2, "memberId");
            return (Criteria) this;
        }

        public Criteria andMemberIdNotBetween(Long value1, Long value2) {
            addCriterion("member_id not between", value1, value2, "memberId");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}