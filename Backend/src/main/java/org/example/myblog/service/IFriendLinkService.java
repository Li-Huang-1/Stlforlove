package org.example.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.myblog.domain.entity.FriendLink;
import org.example.myblog.domain.vo.FriendLinkVO;

import java.util.List;

/**
 * 友链 Service 接口
 */
public interface IFriendLinkService extends IService<FriendLink> {

    /**
     * 查询友链列表（按创建时间正序，先交换的排前面）
     * @return 友链列表（VO，屏蔽 updateTime 等内部字段）
     */
    List<FriendLinkVO> queryFriendLinkList();

    /**
     * 保存友链
     * @param friendLink 友链对象（名称、链接、图标、描述）
     * @return 插入后的展示对象（含主键 id 与自动填充的 createTime）
     */
    FriendLinkVO saveFriendLink(FriendLink friendLink);

    /**
     * 删除友链
     * @param id 友链ID
     */
    void removeFriendLink(Long id);
}
