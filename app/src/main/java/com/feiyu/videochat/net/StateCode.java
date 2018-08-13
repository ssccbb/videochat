package com.feiyu.videochat.net;

/**
 * Created by sung on 2017/12/4.
 *
 * 接口错误列表
 */
public class StateCode {
    public static final String STATE_0000 = "0000";
    /* 用户接口 */
    public static final String STATE_0001 = "0001";
    public static final String STATE_0002 = "0002";
    public static final String STATE_0003 = "0003";
    public static final String STATE_0004 = "0004";
    public static final String STATE_0005 = "0005";
    public static final String STATE_0006 = "0006";
    public static final String STATE_0007 = "0007";
    public static final String STATE_0008 = "0008";
    public static final String STATE_0009 = "0009";
    public static final String STATE_0010 = "0010";
    public static final String STATE_0011 = "0011";
    public static final String STATE_0012 = "0012";
    public static final String STATE_0013 = "0013";
    public static final String STATE_0014 = "0014";
    public static final String STATE_0015 = "0015";
    public static final String STATE_0016 = "0016";
    public static final String STATE_0017 = "0017";
    public static final String STATE_0018 = "0018";
    public static final String STATE_0019 = "0019";
    public static final String STATE_0020 = "0020";
    public static final String STATE_0021 = "0021";
    public static final String STATE_0022 = "0022";
    public static final String STATE_0023 = "0023";
    public static final String STATE_0024 = "0024";
    public static final String STATE_0027 = "0027";
    public static final String STATE_0028 = "0028";
    public static final String STATE_0029 = "0029";
    /* 短信接口 */
    public static final String STATE_0101 = "0101";
    public static final String STATE_0102 = "0102";
    public static final String STATE_0103 = "0103";
    public static final String STATE_0104 = "0104";
    public static final String STATE_0105 = "0105";
    public static final String STATE_0106 = "0106";
    public static final String STATE_0107 = "0107";
    public static final String STATE_0108 = "0108";
    public static final String STATE_0109 = "0109";
    /* 账号接口 */
    public static final String STATE_0201 = "0201";
    public static final String STATE_0203 = "0203";
    public static final String STATE_0204 = "0204";
    public static final String STATE_0205 = "0205";
    public static final String STATE_0206 = "0206";
    public static final String STATE_0211 = "0211";
    public static final String STATE_0212 = "0212";
    public static final String STATE_0213 = "0213";
    public static final String STATE_0214 = "0214";
    public static final String STATE_0215 = "0215";
    public static final String STATE_0216 = "0216";
    public static final String STATE_0217 = "0217";
    public static final String STATE_0220 = "0220";
    public static final String STATE_0221 = "0221";
    public static final String STATE_0222 = "0222";
    public static final String STATE_0223 = "0223";
    public static final String STATE_0226 = "0226";
    public static final String STATE_0227 = "0227";
    public static final String STATE_0228 = "0228";
    public static final String STATE_0229 = "0229";
    public static final String STATE_0230 = "0230";
    public static final String STATE_0231 = "0231";
    public static final String STATE_0232 = "0232";
    public static final String STATE_0233 = "0233";
    public static final String STATE_0234 = "0234";
    public static final String STATE_0241 = "0241";
    public static final String STATE_0242 = "0242";
    public static final String STATE_0243 = "0243";
    public static final String STATE_0244 = "0244";
    public static final String STATE_0245 = "0245";
    /* 游戏接口 */
    public static final String STATE_0301 = "0301";
    public static final String STATE_0302 = "0302";
    public static final String STATE_0303 = "0303";
    public static final String STATE_0304 = "0304";
    public static final String STATE_0305 = "0305";
    public static final String STATE_0306 = "0306";
    public static final String STATE_0307 = "0307";
    public static final String STATE_0310 = "0310";
    /* 消息接口 */
    public static final String STATE_0401 = "0401";
    public static final String STATE_0402 = "0402";
    public static final String STATE_0403 = "0403";
    /* 系统接口 */
    public static final String STATE_0900 = "0900";
    public static final String STATE_0901 = "0901";
    public static final String STATE_0902 = "0902";

    public static String getMessage(String state) {
        switch (state){
            case STATE_0000:
                return "请求成功";
            case STATE_0001:
                return "密码错误";
            case STATE_0002:
                return "短信验证码错误";
            case STATE_0003:
                return "注册失败,此号码已经注册";
            case STATE_0004:
                return "注册申请验证码失败,该手机已注册";
            case STATE_0005:
                return "注册验证码验证失败,该手机未申请验证码";
            case STATE_0006:
                return "认证失败";
            case STATE_0007:
                return "密码不能为空";
            case STATE_0008:
                return "用户不存在";
            case STATE_0009:
                return "地址不存在";
            case STATE_0010:
                return "该号码已绑定其它账号";
            case STATE_0011:
                return "用户设置头像失败";
            case STATE_0012:
                return "生日格式错误";
            case STATE_0013:
                return "游账号数据有误";
            case STATE_0014:
                return "没对应的游戏账号";
            case STATE_0015:
                return "不能删除已充值成功的游戏账号";
            case STATE_0016:
                return "已存在相同记录的游戏账号";
            case STATE_0017:
                return "没有对应的我的游戏";
            case STATE_0018:
                return "没有对应的vip级别";
            case STATE_0019:
                return "没有对应的渠道编码";
            case STATE_0020:
                return "支付宝相关信息不能为空";
            case STATE_0021:
                return "支付宝账号错误";
            case STATE_0022:
                return "支付宝姓名错误";
            case STATE_0023:
                return "支付宝身份证后六位错误";
            case STATE_0024:
                return "没有对应的我的游戏礼包";
            case STATE_0027:
                return "还未设置交易密码";
            case STATE_0028:
                return "交易密码错误";
            case STATE_0029:
                return "24小时内交易密码验证失败已达到5次, 请24小时后再试";
            case STATE_0101:
                return "验证码申请失败!";
            case STATE_0102:
                return "发送短信超上限,请联系管理员!";
            case STATE_0103:
                return "60秒内不能重复发送!";
            case STATE_0104:
                return "请勿频繁请求短信验证码!";
            case STATE_0105:
                return "手机号码格式不正确!";
            case STATE_0106:
                return "验证码过期!请重新获取";
            case STATE_0107:
                return "验证码已使用,请重新申请!";
            case STATE_0108:
                return "验证码错误";
            case STATE_0109:
                return "并未向该手机发送验证码!";
            case STATE_0201:
                return "没有此用户的账号";
            case STATE_0203:
                return "金额有误";
            case STATE_0204:
                return "没有对应的游戏消费订单";
            case STATE_0205:
                return "没有对应的金币充值订单";
            case STATE_0206:
                return "数据查询失败";
            case STATE_0211:
                return "提现金额不可小于100元";
            case STATE_0212:
                return "推广账户不存在";
            case STATE_0213:
                return "账户可用余额不足";
            case STATE_0214:
                return "推广账户信息修改异常";
            case STATE_0215:
                return "可用余额不足";
            case STATE_0216:
                return "交易密码错误";
            case STATE_0217:
                return "密码错误已超过5次，请12小时后再试！";
            case STATE_0220:
                return "游戏帐号不存在";
            case STATE_0221:
                return "用户账户或者推广账户存在问题";
            case STATE_0222:
                return "修改充值用户账户异常";
            case STATE_0226:
                return "请核对游戏账号是否正确";
            case STATE_0227:
                return "游戏错误请联系客服";
            case STATE_0228:
                return "请提醒客服,渠道余额不足,谢谢";
            case STATE_0230:
                return "请提醒客服，游戏ID错误，谢谢！";
            case STATE_0223:
                return "游戏自动充值失败";
            case STATE_0231:
                return "生成订单失败";
            case STATE_0232:
                return "支付方式有误";
            case STATE_0233:
                return "支付用途有误";
            case STATE_0234:
                return "会员等级有误";
            case STATE_0241:
                return "验签失败";
            case STATE_0242:
                return "支付失败";
            case STATE_0243:
                return "充值订单不存在";
            case STATE_0244:
                return "订单处理中";
            case STATE_0245:
                return "未支付";
            case STATE_0301:
                return "没有对应的游戏";
            case STATE_0302:
                return "没有对应的游戏礼包";
            case STATE_0303:
                return "您已领取该礼包三次";
            case STATE_0304:
                return "礼包已没有剩余";
            case STATE_0305:
                return "礼包已失效";
            case STATE_0306:
                return "礼包还不能领取";
            case STATE_0307:
                return "没有对应的游戏主题";
            case STATE_0310:
                return "没有可用的礼包码";
            case STATE_0401:
                return "未知的消费类型";
            case STATE_0402:
                return "未找到对应消息";
            case STATE_0403:
                return "错误的消息操作";
            case STATE_0900:
                return "服务器异常错误";
            case STATE_0901:
                return "参数不能为空";
            case STATE_0902:
                return "没有版本信息";
            default:
                return "未知错误";
        }
    }
}
