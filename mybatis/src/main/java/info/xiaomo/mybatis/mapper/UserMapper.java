package info.xiaomo.mybatis.mapper;


import info.xiaomo.mybatis.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author : xiaomo
 */
@Mapper
@Repository
public interface UserMapper {

    @Results({
            @Result(property = "name", column = "name"),
            @Result(property = "email", column = "email")
    })

    /**
     * 根据名字查
     * @param name
     * @return user
     */
    @Select("SELECT * FROM USER WHERE NAME = #{name}")
    User findByName(@Param("name") String name);

    /**
     * 插入
     *
     * @param name
     * @param email
     * @return
     */
    @Insert("INSERT INTO USER(NAME, EMAIL,PASSWORD,USERNAME) VALUES(#{name}, #{email},#{password},#{username})")
    int insert(@Param("name") String name, @Param("email") Integer email,@Param("password") Integer password,@Param("username") Integer username);

    /**
     * 查所有
     *
     * @return
     */
    @Select("SELECT * FROM USER WHERE 1=1")
    List<User> findAll();

    /**
     * 更新
     *
     * @param user
     */
    @Update("UPDATE USER SET username=#{username} WHERE name=#{name}")
    void update(User user);

    /**
     * 删除
     *
     * @param id
     */
    @Delete("DELETE FROM USER WHERE id =#{id}")
    void delete(Long id);

    /**
     * 添加
     *
     * @param user
     * @return
     */
    @Insert("INSERT INTO USER(name, email,password,username) VALUES(#{name}, #{email},#{password},#{username})")
    int insertByUser(User user);

    /**
     * 添加
     *
     * @param map
     * @return
     */
    @Insert("INSERT INTO user(name, email,password,username) VALUES(#{name,jdbcType=VARCHAR}, #{email,jdbcType=VARCHAR},#{password,jdbcType=VARCHAR},#{username,jdbcType=VARCHAR})")
    int insertByMap(Map<String, Object> map);

}