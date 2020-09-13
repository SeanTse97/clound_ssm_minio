package edu.dgut.mapper;

import edu.dgut.domain.PrivateDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrivateDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pribucket
     *
     * @mbg.generated Sat Sep 12 15:45:17 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pribucket
     *
     * @mbg.generated Sat Sep 12 15:45:17 CST 2020
     */
    int insert(PrivateDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pribucket
     *
     * @mbg.generated Sat Sep 12 15:45:17 CST 2020
     */
    int insertSelective(PrivateDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pribucket
     *
     * @mbg.generated Sat Sep 12 15:45:17 CST 2020
     */
    PrivateDO selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pribucket
     *
     * @mbg.generated Sat Sep 12 15:45:17 CST 2020
     */
    int updateByPrimaryKeySelective(PrivateDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pribucket
     *
     * @mbg.generated Sat Sep 12 15:45:17 CST 2020
     */
    int updateByPrimaryKey(PrivateDO record);

    List<PrivateDO> getByName(String name);

    List<PrivateDO> getPage(@Param("name") String name,@Param("page") Integer page);
}