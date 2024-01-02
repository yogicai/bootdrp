package com.bootdo.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.bootdo.common.dao.DictDao;
import com.bootdo.common.domain.DictDO;
import com.bootdo.common.service.DictService;
import com.bootdo.data.dao.StockDao;
import com.bootdo.data.domain.StockDO;
import com.bootdo.system.domain.UserDO;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class DictServiceImpl implements DictService {
    @Autowired
    private DictDao dictDao;
    @Autowired
    private StockDao stockDao;

    @Override
    public DictDO get(Long id) {
        return dictDao.get(id);
    }

    @Override
    public List<DictDO> list(Map<String, Object> map) {
        return dictDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return dictDao.count(map);
    }

    @Override
    public int save(DictDO sysDict) {
        return dictDao.save(sysDict);
    }

    @Override
    public int update(DictDO sysDict) {
        return dictDao.update(sysDict);
    }

    @Override
    public int remove(Long id) {
        return dictDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return dictDao.batchRemove(ids);
    }

    @Override
    public List<DictDO> listType() {
        return dictDao.listType();
    }

    @Override
    public List<DictDO> getHobbyList(UserDO userDO) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", "hobby");
        List<DictDO> hobbyList = dictDao.list(param);

        if (StrUtil.isNotEmpty(userDO.getHobby())) {
            String userHobbys[] = userDO.getHobby().split(";");
            for (String userHobby : userHobbys) {
                for (DictDO hobby : hobbyList) {
                    if (!Objects.equals(userHobby, hobby.getId().toString())) {
                        continue;
                    }
                    hobby.setRemarks("true");
                    break;
                }
            }
        }

        return hobbyList;
    }

    @Override
    public List<DictDO> getSexList() {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", "sex");
        return dictDao.list(param);
    }

    @Override
    public String getName(String type, String value) {
        Map<String, Object> param = new HashMap<String, Object>(16);
        param.put("type", type);
        param.put("value", value);
        String rString = dictDao.list(param).get(0).getName();
        return rString;
    }

    @Override
    public Map<String, List<DictDO>> lists(Map<String, Object> map) {
        Map<String, List<DictDO>> maps = new HashMap<>();
        List<DictDO> list = dictDao.list(map);
        for (DictDO dic : list) {
            if (!maps.containsKey(dic.getType())) {
                maps.put(dic.getType(), new ArrayList<>());
            }
            maps.get(dic.getType()).add(dic);
        }
        return maps;
    }

    @Override
    public Map<String, List<Map<String, String>>> listExtra(Map<String, Object> map) {
        Map<String, List<Map<String, String>>> maps = new HashMap<>();
        List<StockDO> list = stockDao.list(map);
        List<Map<String, String>> listMap = Lists.newArrayList();
        for (StockDO stockDO : list) {
            listMap.add(ImmutableMap.of("name", stockDO.getStockName(), "value", stockDO.getStockNo()));
        }
        maps.put("data_stock", listMap);
        return maps;
    }
}
