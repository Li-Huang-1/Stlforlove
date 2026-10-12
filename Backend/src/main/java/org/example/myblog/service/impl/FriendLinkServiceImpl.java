package org.example.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.myblog.domain.entity.FriendLink;
import org.example.myblog.domain.vo.FriendLinkVO;
import org.example.myblog.mapper.FriendLinkMapper;
import org.example.myblog.service.IFriendLinkService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 友链 Service 实现
 */
@Service
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements IFriendLinkService {

    /** 列表最多返回条数，避免全表扫描 */
    private static final int MAX_LIST_SIZE = 200;

    @Autowired
    private FriendLinkMapper mapper;

    @Override
    public List<FriendLinkVO> queryFriendLinkList() {
        // 构造查询条件：按创建时间正序（先交换的排前面），取最近 MAX_LIST_SIZE 条
        LambdaQueryWrapper<FriendLink> queryWrapper = new LambdaQueryWrapper<FriendLink>()
                .orderByAsc(FriendLink::getCreateTime)
                .last("LIMIT " + MAX_LIST_SIZE);

        // 执行查询
        List<FriendLink> friendLinks = mapper.selectList(queryWrapper);

        // Entity -> VO（for 循环，屏蔽 updateTime 等内部字段）
        List<FriendLinkVO> voList = new ArrayList<>();
        for (FriendLink friendLink : friendLinks) {
            FriendLinkVO vo = new FriendLinkVO();
            BeanUtils.copyProperties(friendLink, vo);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public FriendLinkVO saveFriendLink(FriendLink friendLink) {

        // 去除首尾空白后写回
        friendLink.setName(friendLink.getName().trim());
        friendLink.setUrl(friendLink.getUrl().trim());
        if (friendLink.getLogo() != null) {
            friendLink.setLogo(friendLink.getLogo().trim());
        }
        if (friendLink.getDescription() != null) {
            friendLink.setDescription(friendLink.getDescription().trim());
        }

        // 执行插入：mapper.insert 会回填自增主键 id，FieldHandler 自动填充 createTime/updateTime
        mapper.insert(friendLink);

        // Entity -> VO（屏蔽 updateTime，对外只暴露展示字段）
        FriendLinkVO vo = new FriendLinkVO();
        BeanUtils.copyProperties(friendLink, vo);
        return vo;
    }

    @Override
    public void removeFriendLink(Long id) {
        mapper.deleteById(id);
    }
}
