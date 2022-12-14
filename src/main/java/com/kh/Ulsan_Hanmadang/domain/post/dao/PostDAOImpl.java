package com.kh.Ulsan_Hanmadang.domain.post.dao;

import com.kh.Ulsan_Hanmadang.domain.EventInfo;
import com.kh.Ulsan_Hanmadang.domain.FacInfo;
import com.kh.Ulsan_Hanmadang.domain.PEvent;
import com.kh.Ulsan_Hanmadang.domain.PromotionHome;
import com.kh.Ulsan_Hanmadang.domain.post.Post;
import com.kh.Ulsan_Hanmadang.domain.post.Promotion;
import com.kh.Ulsan_Hanmadang.domain.post.Reply;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@ToString
@Slf4j
@Repository
@RequiredArgsConstructor
public class PostDAOImpl implements PostDAO{

  private final JdbcTemplate jt;

  //μκΈμμ±
  @Override
  public Long saveOrigin(Post post) {
    StringBuffer sql = new StringBuffer();
    sql.append("insert into post (post_id,pcategory,title,email,nickname,pcontent ");
    sql.append(" ) ");
    sql.append("values(post_post_id_seq.nextval,?,?,?,?,?) ");


    KeyHolder keyHolder = new GeneratedKeyHolder();
    jt.update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(sql.toString(), new String[]{"post_id"});
        pstmt.setString(1,post.getPcategory());
        pstmt.setString(2, post.getTitle());
        pstmt.setString(3, post.getEmail());
        pstmt.setString(4, post.getNickname());
        pstmt.setString(5, post.getPcontent());
        return pstmt;
      }
    },keyHolder);

    return Long.valueOf(keyHolder.getKeys().get("post_id").toString());
  }

  //λͺ©λ‘
  @Override
  public List<Post> findAll() {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT ");
    sql.append("  post_id, ");
    sql.append("  pcategory, ");
    sql.append("  title, ");
    sql.append("  email, ");
    sql.append("  nickname, ");
    sql.append("  hit, ");
    sql.append("  pcontent, ");
    sql.append("  cdate, ");
    sql.append("  udate ");
    sql.append("FROM ");
    sql.append("  post ");
    sql.append("Order by post_id desc ");

    List<Post> list = jt.query(sql.toString(), new BeanPropertyRowMapper<>(Post.class));

    return list;
  }

  //μΉ΄νκ³ λ¦¬λ³ λͺ©λ‘
  @Override
  public List<Post> findAll(String category) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT ");
    sql.append("  post_id, ");
    sql.append("  pcategory, ");
    sql.append("  title, ");
    sql.append("  email, ");
    sql.append("  nickname, ");
    sql.append("  hit, ");
    sql.append("  pcontent, ");
    sql.append("  cdate, ");
    sql.append("  udate ");
    sql.append("FROM ");
    sql.append("  post ");
    sql.append("WHERE pcategory = ? ");
    sql.append("Order by post_id desc ");

    List<Post> list = jt.query(sql.toString(), new BeanPropertyRowMapper<>(Post.class),category);

    return list;
  }

  @Override
  public List<Post> findAll(int startRec, int endRec) {
    StringBuffer sql = new StringBuffer();
    sql.append("select t1.* ");
    sql.append("from( ");
    sql.append("    SELECT ");
    sql.append("    ROW_NUMBER() OVER (ORDER BY post_id DESC) no, ");
    sql.append("    post_id, ");
    sql.append("    pcategory, ");
    sql.append("    title, ");
    sql.append("    email, ");
    sql.append("    nickname, ");
    sql.append("    hit, ");
    sql.append("    pcontent, ");
    sql.append("    cdate, ");
    sql.append("    udate ");
    sql.append("    FROM post) t1 ");
    sql.append("where t1.no between ? and ? ");

    List<Post> list = jt.query(
            sql.toString(),
            new BeanPropertyRowMapper<>(Post.class),
            startRec, endRec
    );
    return list;
  }

  @Override
  public List<Post> findAll(String category, int startRec, int endRec) {
    StringBuffer sql = new StringBuffer();
    sql.append("select t1.* ");
    sql.append("from( ");
    sql.append("    SELECT ");
    sql.append("      ROW_NUMBER() OVER (ORDER BY post_id DESC) no, ");
    sql.append("      post_id, ");
    sql.append("      pcategory, ");
    sql.append("      title, ");
    sql.append("      email, ");
    sql.append("      nickname, ");
    sql.append("      hit, ");
    sql.append("      pcontent, ");
    sql.append("      cdate, ");
    sql.append("      udate ");
    sql.append("    FROM post ");
    sql.append("   where pcategory = ? ) t1 ");
    sql.append("where t1.no between ? and ? ");

    List<Post> list = jt.query(
            sql.toString(),
            new BeanPropertyRowMapper<>(Post.class),
            category, startRec, endRec
    );
    return list;
  }
  @Override
  public List<EventInfo> findAllEvents(int startRec, int endRec) {
    StringBuffer sql = new StringBuffer();
    sql.append("select t1.* ");
    sql.append("from( ");
    sql.append("    SELECT ");
    sql.append("      ROW_NUMBER() OVER (ORDER BY event_id DESC) no, ");
    sql.append("      event_id, ");
    sql.append("      mt20id, ");
    sql.append("      prfnm, ");
    sql.append("      prfpdfrom, ");
    sql.append("      prfpdto, ");
    sql.append("      fcltynm, ");
    sql.append("      prfcast, ");
    sql.append("      prfcrew, ");
    sql.append("      prfruntime, ");
    sql.append("      prfage, ");
    sql.append("      entrpsnm, ");
    sql.append("      pcseguidance, ");
    sql.append("      poster, ");
    sql.append("      genrenm, ");
    sql.append("      prfstate ");
    sql.append("    from p_event) t1 ");
    sql.append("where t1.no between ? and ? ");

    List<EventInfo> result = jt.query(
            sql.toString(),
            new BeanPropertyRowMapper<>(EventInfo.class),
            startRec,
            endRec);
    try {
      log.info("Evt query = {}", result.size());
    } catch (NullPointerException e) {
      log.info("λν¬μΈνΈ");
    }
    return result;
  }

  //κ²μ
  @Override
  public List<Post> findAll(PostFilterCondition filterCondition) {
    StringBuffer sql = new StringBuffer();
    sql.append("select t1.* ");
    sql.append("from( ");
    sql.append("    SELECT  ROW_NUMBER() OVER (ORDER BY post_id DESC) no, ");
    sql.append("            post_id, ");
    sql.append("            pcategory, ");
    sql.append("            title, ");
    sql.append("            email, ");
    sql.append("            nickname, ");
    sql.append("            hit, ");
    sql.append("            pcontent, ");
    sql.append("            cdate, ");
    sql.append("            udate ");
    sql.append("      FROM post ");
    sql.append("     WHERE ");

    //λΆλ₯
    sql = dynamicQuery(filterCondition, sql);

    sql.append(") t1 ");
    sql.append("where t1.no between ? and ? ");

    List<Post> list = null;

    //κ²μν μ μ²΄
//    if(StringUtils.isEmpty(filterCondition.getCategory())){
//      list = jt.query(
//          sql.toString(),
//          new BeanPropertyRowMapper<>(post.class),
//          filterCondition.getStartRec(),
//          filterCondition.getEndRec()
//      );
    //κ²μν λΆλ₯
//    }else{
    list = jt.query(
            sql.toString(),
            new BeanPropertyRowMapper<>(Post.class),
            filterCondition.getCategory(),
            filterCondition.getStartRec(),
            filterCondition.getEndRec()
    );
//    }

    return list;
  }

  @Override
  public List<EventInfo> findAllEvents(PostFilterCondition filterCondition) {
    StringBuffer sql = new StringBuffer();
    sql.append("select t1.* ");
    sql.append("from( ");
    sql.append("    SELECT  ROW_NUMBER() OVER (ORDER BY event_id DESC) no, ");
    sql.append("            event_id, ");
    sql.append("            mt20id, ");
    sql.append("            prfnm, ");
    sql.append("            prfpdfrom, ");
    sql.append("            prfpdto, ");
    sql.append("            fcltynm, ");
    sql.append("            prfcast, ");
    sql.append("            prfcrew, ");
    sql.append("            prfruntime, ");
    sql.append("            prfage, ");
    sql.append("            entrpsnm, ");
    sql.append("            pcseguidance, ");
    sql.append("            poster, ");
    sql.append("            sty, ");
    sql.append("            genrenm, ");
    sql.append("            prfstate, ");
    sql.append("            dtguidance ");
    sql.append("      FROM p_event ");
    sql.append("     WHERE ");

    //λΆλ₯
    sql = dynamicQueryForEvent(filterCondition, sql);
    sql.append(") t1 ");
    sql.append("where t1.no between ? and ? ");

    List<EventInfo> list = null;
    list = jt.query(
            sql.toString(),
            new BeanPropertyRowMapper<>(EventInfo.class),
            filterCondition.getStartRec(),
            filterCondition.getEndRec()
    );
    return list;
  }

  //μ‘°ν
  @Override
  public Post findByPostId(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT  ");
    sql.append("  post_id, ");
    sql.append("  pcategory,  ");
    sql.append("  title,  ");
    sql.append("  email,  ");
    sql.append("  nickname, ");
    sql.append("  hit,  ");
    sql.append("  pcontent, ");
    sql.append("  cdate,  ");
    sql.append("  udate ");
    sql.append("FROM  ");
    sql.append("  post ");
    sql.append("where post_id = ?  ");

    Post postItem = null;
    try {
      postItem = jt.queryForObject(
              sql.toString(),
              new BeanPropertyRowMapper<>(Post.class),
              id);
    }catch (Exception e){ // 1κ±΄μ λͺ»μ°ΎμΌλ©΄
      postItem = null;
    }

    return postItem;
  }

  @Override
  public EventInfo findByEventId(Long id) {

    StringBuffer sql = new StringBuffer();
    sql.append("SELECT  ");
    sql.append("  event_id, ");
    sql.append("  mt20id,  ");
    sql.append("  prfnm,  ");
    sql.append("  prfpdfrom,  ");
    sql.append("  prfpdto, ");
    sql.append("  fcltynm,  ");
    sql.append("  prfcast, ");
    sql.append("  prfcrew,  ");
    sql.append("  prfruntime, ");
    sql.append("  prfage, ");
    sql.append("  entrpsnm,  ");
    sql.append("  pcseguidance, ");
    sql.append("  poster,  ");
    sql.append("  sty,  ");
    sql.append("  genrenm,  ");
    sql.append("  prfstate,  ");
    sql.append("  openrun,  ");
    sql.append("  mt10id,  ");
    sql.append("  dtguidance ");
    sql.append("FROM  ");
    sql.append("  p_event ");
    sql.append("where event_id = ?  ");

    EventInfo eventInfo = null;
    try {
      eventInfo = jt.queryForObject(
              sql.toString(),
              new BeanPropertyRowMapper<>(EventInfo.class),
              id);
    }catch (Exception e){ // 1κ±΄μ λͺ»μ°ΎμΌλ©΄
      eventInfo = null;
    }

    return eventInfo;
  }


  @Override
  public FacInfo findByFacId(String id) {

    log.info("id={}", id);
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT  ");
    sql.append("  mt10id, ");
    sql.append("  fcltynm,  ");
    sql.append("  mt13cnt,  ");
    sql.append("  fcltychartr,  ");
    sql.append("  seatscale, ");
    sql.append("  telno,  ");
    sql.append("  relateurl, ");
    sql.append("  adres,  ");
    sql.append("  opende, ");
    sql.append("  la, ");
    sql.append("  lo  ");
    sql.append("FROM  ");
    sql.append("  p_facility ");
    sql.append("where mt10id = ? ");

    FacInfo facInfo = null;
    try {
      facInfo = jt.queryForObject(
              sql.toString(),
              new BeanPropertyRowMapper<>(FacInfo.class),
              id);
    }catch (Exception e){ // 1κ±΄μ λͺ»μ°ΎμΌλ©΄
      facInfo = null;
    }

    return facInfo;
  }

  //μ­μ 
  @Override
  public int deleteByPostId(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE FROM post ");
    sql.append(" WHERE post_id = ? ");

    int updateItemCount = jt.update(sql.toString(), id);

    return updateItemCount;
  }

  //μμ 
  @Override
  public int updateByPostId(Long id, Post post) {

    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE post ");
    sql.append("   SET pcategory = ?, ");
    sql.append("       title = ?, ");
    sql.append("       pcontent = ?, ");
    sql.append("       udate = systimestamp ");
    sql.append(" WHERE post_id = ? ");

    int updatedItemCount = jt.update(
            sql.toString(),
            post.getPcategory(),
            post.getTitle(),
            post.getPcontent(),
            id
    );

    return updatedItemCount;
  }

  @Override
  public String facilityLink(String facid) {

    return null;
  }

  //λκΈ λ±λ‘
  @Override
  public Long saveReply(Reply reply) {

//    log.info("r={}", reply);
    StringBuffer sql = new StringBuffer();
    sql.append("insert into reply (reply_id,post_id,pcategory,email,nickname,rcontent) ");
    sql.append("values(reply_reply_seq.nextval,?,?,?,?,?) ");

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jt.update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(sql.toString(), new String[]{"reply_id"});
        pstmt.setLong(1, reply.getPostId());
        pstmt.setString(2, reply.getPcategory());
        pstmt.setString(3, reply.getEmail());
        pstmt.setString(4, reply.getNickname());
        pstmt.setString(5, reply.getRcontent());
        return pstmt;
      }
    },keyHolder);

    return Long.valueOf(keyHolder.getKeys().get("reply_id").toString());
  }

  @Override
  public Optional<Reply> findReplyById(Long rid) {
    StringBuffer sql = new StringBuffer();
    sql.append("select rcontent ");
    sql.append("  from reply ");
    sql.append(" where rid = ? ");

    Reply findedReply = null;
    try {
      findedReply = jt.queryForObject(
              sql.toString(),
              new BeanPropertyRowMapper<>(Reply.class),
              rid);
      return Optional.of(findedReply);
    }catch (EmptyResultDataAccessException e){ // 1κ±΄μ λͺ»μ°ΎμΌλ©΄
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public int updateReplyById(Long rid, String rcontent) {

    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE reply ");
    sql.append("   SET rcontent = ?, ");
    sql.append("       udate = systimestamp ");
    sql.append(" WHERE reply_id = ? ");

    int updatedItemCount = jt.update(
            sql.toString(),
            rcontent,
            rid
    );

    return updatedItemCount;
  }

  @Override
  public int deleteReplyById(Long rid) {
    StringBuffer sql = new StringBuffer();
    sql.append("DELETE FROM reply ");
    sql.append(" WHERE reply_id = ? ");

    int updateItemCount = jt.update(sql.toString(), rid);

    return updateItemCount;
  }

  @Override
  public List<Reply> findReplyByPostId(Long postId) {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT ");
    sql.append("  reply_id, ");
//    sql.append("  post_id, ");
    sql.append("  pcategory, ");
    sql.append("  email, ");
    sql.append("  nickname, ");
    sql.append("  rcontent, ");
    sql.append("  cdate, ");
    sql.append("  udate ");
    sql.append("FROM ");
    sql.append("  reply ");
    sql.append("WHERE post_id = ? ");
    sql.append("Order by reply_id desc ");

    List<Reply> list = jt.query(sql.toString(), new BeanPropertyRowMapper<>(Reply.class), postId);

    return list;
  }


  @Override
  public List<PEvent> getDisplyEvents(String date) {
    StringBuffer sql = new StringBuffer();
    sql.append("select t1.* ");
    sql.append("  from (select row_number() over (order by prfpdto) no, ");
    sql.append("               poster ");
    sql.append("          from p_event ");
    sql.append("         where to_date(?) < to_date(prfpdto)) t1 ");
    sql.append(" where t1.no < 7");

    List<PEvent> list = jt.query(sql.toString(), new BeanPropertyRowMapper<>(PEvent.class), date);

    return list;
  }

  @Override
  public int getDisplyEventCount(String date) {
    StringBuffer sql = new StringBuffer();
    sql.append("select count(*) ");
    sql.append("  from p_event ");
    sql.append(" where to_date(?) < to_date(prfpdto) ");

    Integer cnt = jt.queryForObject(sql.toString(), Integer.class, date);
//    log.info("Event size ={}", cnt);
    return cnt;
  }

  //νλ³΄μ λ³΄

  @Override
  public Promotion getPromotionInfoById(Long pid) {

    StringBuffer sql = new StringBuffer();
    sql.append("select * ");
    sql.append("  from promotion ");
    sql.append(" where post_id = ? ");

    Promotion findedPromotion = null;

    findedPromotion = jt.queryForObject(
              sql.toString(),
              new BeanPropertyRowMapper<>(Promotion.class),
              pid);
    return findedPromotion;
  }

  //μ‘°νμμ¦κ°
  @Override
  public int increaseHitCount(Long id) {
    StringBuffer sql = new StringBuffer();
    sql.append("update post  ");
    sql.append("set hit = hit + 1 ");
    sql.append("where post_id = ? ");

    int affectedRows = jt.update(sql.toString(), id);

    return affectedRows;
  }


  //μ’μμμ¦κ°
  @Override
  public int increaseGoodCount(Long id) {
    return 0;
  }

  //μ μ²΄κ±΄μ
  @Override
  public int totalCount() {

    String sql = "select count(*) from post";

    Integer cnt = jt.queryForObject(sql, Integer.class);

    return cnt;
  }

  @Override
  public int totalPEventCount() {

    String sql = "select count(*) from p_event";

    Integer cnt = jt.queryForObject(sql, Integer.class);
//    log.info("Event size ={}", cnt);
    return cnt;
  }

  @Override
  public int totalCount(String bcategory) {

    String sql = "select count(*) from post where pcategory = ? ";

    Integer cnt = jt.queryForObject(sql, Integer.class, bcategory);

    return cnt;
  }

  @Override
  public int totalCount(PostFilterCondition filterCondition) {

    StringBuffer sql = new StringBuffer();

    sql.append("select count(*) ");
    sql.append("  from post  ");
    sql.append(" where  ");

    sql = dynamicQuery(filterCondition, sql);

    Integer cnt = 0;
    //κ²μν μ μ²΄ κ²μ κ±΄μ
    if(StringUtils.isEmpty(filterCondition.getCategory())) {
      cnt = jt.queryForObject(
              sql.toString(), Integer.class
      );
      //κ²μν λΆλ₯λ³ κ²μ κ±΄μ
    }else{
      cnt = jt.queryForObject(
              sql.toString(), Integer.class,
              filterCondition.getCategory()
      );
    }

    return cnt;
  }

  @Override
  public int totalEventCount(PostFilterCondition filterCondition) {
    StringBuffer sql = new StringBuffer();

    sql.append("select count(*) ");
    sql.append("  from p_event  ");
    sql.append(" where  ");

    sql = dynamicQueryForEvent(filterCondition, sql);

    Integer cnt = 0;
    //κ²μν μ μ²΄ κ²μ κ±΄μ
    if(StringUtils.isEmpty(filterCondition.getCategory())) {
      cnt = jt.queryForObject(
              sql.toString(), Integer.class
      );
      //κ²μν λΆλ₯λ³ κ²μ κ±΄μ
    }else{
      cnt = jt.queryForObject(
              sql.toString(), Integer.class,
              filterCondition.getCategory()
      );
    }

    return cnt;
  }

  private StringBuffer dynamicQuery(PostFilterCondition filterCondition, StringBuffer sql) {
    //λΆλ₯
    if(StringUtils.isEmpty(filterCondition.getCategory())){

    }else{
      log.info("dnmic query cate={}", filterCondition.getCategory());
      sql.append("       pcategory = ? ");
    }

    //λΆλ₯,κ²μμ ν,κ²μμ΄ μ‘΄μ¬
    if(!StringUtils.isEmpty(filterCondition.getCategory()) &&
            !StringUtils.isEmpty(filterCondition.getSearchType()) &&
            !StringUtils.isEmpty(filterCondition.getKeyword())){

      sql.append(" AND ");
    }

    //κ²μμ ν
    switch (filterCondition.getSearchType()){
      case "TC":  //μ λͺ© + λ΄μ©
        sql.append("    (  title    like '%"+ filterCondition.getKeyword()+"%' ");
        sql.append("    or pcontent like '%"+ filterCondition.getKeyword()+"%' )");
        break;
      case "T":   //μ λͺ©
        sql.append("       title    like '%"+ filterCondition.getKeyword()+"%' ");
        break;
      case "C":   //λ΄μ©
        sql.append("       pcontent like '%"+ filterCondition.getKeyword()+"%' ");
        break;
      case "E":   //μμ΄λ(μ΄λ©μΌ)
        sql.append("       email    like '%"+ filterCondition.getKeyword()+"%' ");
        break;
      case "N":   //λ³μΉ­
        sql.append("       nickname like '%"+ filterCondition.getKeyword()+"%' ");
        break;
      default:
    }
    return sql;
  }

  private StringBuffer dynamicQueryForEvent(PostFilterCondition filterCondition, StringBuffer sql) {

    //κ²μμ ν
    switch (filterCondition.getSearchType()){
      case "ETC":   //μ΄λ²€νΈ μ λͺ©+λ΄μ©
        sql.append("       prfnm    like '%"+ filterCondition.getKeyword()+"%' ");
        sql.append("    or sty like '%"+ filterCondition.getKeyword()+"%' )");
        break;
      case "ET":  //μ΄λ²€νΈ μ λͺ©
        sql.append("       prfnm    like '%"+ filterCondition.getKeyword()+"%' ");
        break;
      case "EC":  //μ΄λ²€νΈ λ΄μ©
        sql.append("       sty like '%"+ filterCondition.getKeyword()+"%' ");
        break;
      case "EG":  //μ΄λ²€νΈ μ₯λ₯΄
        sql.append("       genrenm like '%"+ filterCondition.getKeyword()+"%' ");
        break;
      case "ED":  //μ΄λ²€νΈ μΌμ
        sql.append("       to_date(prfpdfrom) <= '" + filterCondition.getKeyword() + "' ");
        sql.append("       and to_date(prfpdto) >= '" + filterCondition.getKeyword() + "' ");
        break;
      case "EA":  //μ΄λ²€νΈ κ΄λ κ°λ₯ μ°λ Ή
        sql.append("       prfage like '%"+ filterCondition.getKeyword()+"%' ");
        break;

      default:
    }
    return sql;
  }

  /**
   * λ΄κ° μ΄ κΈ
   * @param email μμ΄λ
   * @return
   */
  @Override
  public List<Post> myPost(String email) {
    StringBuffer sql = new StringBuffer();
    sql.append("select post_id, title, cdate from post where email = ? ");
    sql.append("order by cdate desc ");

    List<Post> list = jt.query(sql.toString(), new BeanPropertyRowMapper<>(Post.class), email);

    return list;
  }

  /**
   * νλ³΄μ λ³΄λ±λ‘
   *
   * @param promotion νλ³΄μ λ³΄
   * @return λ±λ‘κ±΄μ
   */
  @Override
  public int savePromotionData(Promotion promotion) {
    int result = 0;
    StringBuffer sql = new StringBuffer();
    sql.append("insert into promotion (promotion_id,post_id,ad_start_day,ad_end_day,ent_fee) ");
    sql.append(" values (?,?,?,?,?) ");

    result = jt.update(sql.toString(), promotion.getPromotionId(), promotion.getPostId(),
        promotion.getAdStartDay(), promotion.getAdEndDay(), promotion.getEntFee());

    return result;
  }

  /**
   * νλ³΄μ λ³΄μ‘°ν
   *
   * @param pid νλ³΄κ²μλ¬Όλ²νΈ
   * @return νλ³΄μ λ³΄
   */
  @Override
  public Promotion findByPromotionId(Long pid) {
    StringBuffer sql = new StringBuffer();
    sql.append("select * ");
    sql.append("  from promotion ");
    sql.append(" where promotion_id = ? ");

    Promotion promotion = null;
    try {
      promotion = jt.queryForObject(
          sql.toString(),
          new BeanPropertyRowMapper<>(Promotion.class),
          pid);
    }catch (Exception e){ // 1κ±΄μ λͺ»μ°ΎμΌλ©΄
      promotion = null;
    }
    return promotion;
  }

  /**
   * κ²μλ¬Ό λ²νΈ μμ±
   *
   * @return κ²μλ¬Όλ²νΈ
   */
  @Override
  public Long generatePostId() {
    String sql = "select post_post_id_seq.currval from dual ";
    Long postId = jt.queryForObject(sql, Long.class);
    log.info("curr={}", postId);
    return postId;
  }

  @Override
  public Long generatePromId() {
    String sql = "select promotion_promotion_id_seq.nextval from dual ";
    Long postId = jt.queryForObject(sql, Long.class);
    log.info("curr={}", postId);
    return postId;
  }


  /**
   * ννλ©΄μ λμ€νλ μ΄ν  νλ³΄μ΄λ―Έμ§ λͺ©λ‘
   *
   * @param date νμ¬ μκ°
   * @return μ΄λ―Έμ§ λͺ©λ‘
   */
  @Override
  public List<PromotionHome> getDisplyProms(String date) {
    StringBuffer sql = new StringBuffer();
    sql.append("select t1.* ");
    sql.append("from ( ");
    sql.append("        select row_number() over (order by ad_end_day) no, u1.uploadfile_id, p1.post_id, u1.rid, ");
    sql.append("        u1.upload_filename ");
    sql.append("        from promotion p1, uploadfile u1 ");
    sql.append("        where p1.post_id=u1.rid and u1.code = 'B0102' ");
    sql.append("        and to_date(?) < to_date(ad_end_day) ");
    sql.append("     ) t1 ");
    sql.append("where t1.no <7 ");

    List<PromotionHome> list = jt.query(sql.toString(), new BeanPropertyRowMapper<>(PromotionHome.class), date);

    return list;
  }
}
