package com.bossien.train.util;

import java.util.ArrayList;
import java.util.List;

/**
 * http://www.sudu.cn/info/html/edu/java/20080403/259431.html
 * http://www.cnblogs.com/hedonister/archive/2006/11/21/567383.html
 * 
 * 权限控制算法
 * 
 * 这里我介绍一种很常用，也比较Professor的权限控制思路。 这里我用java语言描述，其实都差不多的。自己转一下就可以了。
 * 为了方便，我们这里定义a^b为：a的b次方 这里，我们为每一个操作设定一个唯一的整数值，比如：
 * 
 * 删除Ａ－－－0 修改Ａ－－－1 添加Ａ－－－2
 * 
 * 删除Ｂ－－－3 修改Ｂ－－－4 添加Ｂ－－－5 。。。
 * 
 * 理论上可以有Ｎ个操作，这取决于你用于储存用户权限值的数据类型了。
 * 
 * 这样，如果用户有权限：添加Ａ－－－2；删除Ｂ－－－3；修改Ｂ－－－4 那用户的权限值 purview =2^2+2^3+2^4＝28，也就是2的权的和了。
 * 化成二进制可以表示为11100 这样，如果要验证用户是否有删除Ｂ的权限，就可以通过位与运算来实现。 在Ｊａｖａ里，位与运算运算符号为＆ 即是：int
 * value = purview &((int)Math.pow(2,3)); 你会发现，当用户有操作权限时，运算出来的结果都会等于这个操作需要的权限值！
 * 
 * 原理： 位与运算，顾名思义就是对位进行与运算： 以上面的式子为例：purview & 2^3 也就是　28&8 将它们化成二进制有 　 11100 ＆
 * 01000 ------------------- 　 01000 == 8(十进制)　＝＝　2^3 同理，如果要验证是否有删除Ａ－－－0的权限
 * 可以用：purview &((int)Math.pow(2,0)); 即： 　 11100 ＆ 00001
 * ------------------------ 　 00000 == 0(十进制)　　！＝　2^0
 * 
 * 这种算法的一个优点是速度快。可以同时处理Ｎ个权限 如果想验证是否同时有删除Ａ－－－0和删除Ｂ－－－3的权限
 * 可以用purview&(2^0+2^3)==(2^0+2^3)?true:false; 设置多角色用户。根据权限值判断用户的角色。。。
 * 
 * 下面提供一个java的单操作权限判断的代码：
 * 
 * //userPurview是用户具有的总权限 //optPurview是一个操作要求的权限为一个整数（没有经过权的！） public static
 * boolean checkPower(int userPurview, int optPurview) { 
 * 		int purviewValue = (int)Math.pow(2, optPurview); 
 * 		return (userPurview & purviewValue) == purviewValue; 
 * }
 * 
 * 当然，多权限的验证只要扩展一下就可以了。
 * 几点注意事项：首先，一个系统可能有很多的操作，因此，请建立数据字典，以便查阅，修改时使用。其次，如果用数据库储存用户权限
 * ，请注意数值的有效范围。操作权限值请用唯一的整数！
 * 
 * @author Administrator
 * 
 */
public class PurviewUtil {

	// 计算菜单集合的权限值
	public static Long getPower(List<Integer> list) {
		Double purviewValue = 0d;
		if(null==list || list.isEmpty()) {
            return purviewValue.longValue();
        }
		for(int purview: list){
			purviewValue += Math.pow(2, purview);
		}
		return purviewValue.longValue();
	}
	
	/**
	 * 校验权限
	 * @param userPurview 用户/单位 具有的总权限
	 * @param optPurview 一个操作要求的权限为一个整数
	 * @return
	 */
	public static boolean checkPower(Long userPurview, int optPurview)
	{
		int purviewValue = (int)Math.pow(2, optPurview);
		return (userPurview & purviewValue) == purviewValue;
	}
	
	public static void main(String[] args) {
		// 测试权限
		List<Integer> list = new ArrayList<Integer>();
		list.add(4);
		list.add(8);
		System.out.println(PurviewUtil.getPower(list));// 36 = 2的4次方 + 2的8次方 = 16 + 256 = 272
		
		// 检测权限
		boolean checkPower01 = PurviewUtil.checkPower(272L, 4);
		System.out.println(checkPower01);
		
		boolean checkPower02 = PurviewUtil.checkPower(272L, 8);
		System.out.println(checkPower02);
		
		boolean checkPower03 = PurviewUtil.checkPower(272L, 2);
		System.out.println(checkPower03);
		
	}
}
