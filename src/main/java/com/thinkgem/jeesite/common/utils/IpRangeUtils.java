package com.thinkgem.jeesite.common.utils;

public class IpRangeUtils {

	//集合4种类型进行判断
	public static boolean isInRanges(String ip, String cidr) throws Exception {
		cidr = cidr.replace(" ", "");
		cidr = cidr.replaceAll("\r\n", "");
		String[] ipAddr = cidr.split(";");
		boolean flag = false;
		for (int i = 0; i < ipAddr.length; i++) {
			int idx = ipAddr[i].indexOf("-");
			if (idx == -1) {
				//其他三种类型
				flag = isInRangeTwo(ip, ipAddr[i]);
			} else {
				//属于类型（1）###.###.###.### - ###.###.###.###
				flag = isInRange(ip, ipAddr[i]);
			}
			if (flag) {
				break;
			} else {
				continue;
			}
		}
		return flag;
	}

	//类型（1）###.###.###.### - ###.###.###.###
	public static boolean isInRange(String ip, String cidr) throws Exception {

		int idx = cidr.indexOf('-');
		String startip = cidr.substring(0, idx);
		String endip = cidr.substring(idx + 1);

		String[] ips = ip.split("\\.");
		String[] startips = startip.split("\\.");
		String[] endips = endip.split("\\.");
		boolean flag = true;
		for (int i = 0; i < ips.length; i++) {
			try {
				if (Integer.parseInt(ips[i]) >= Integer.parseInt(startips[i]) && Integer.parseInt(ips[i]) <= Integer.parseInt(endips[i])) {
					continue;
				} else {
					flag = false;
					break;
				}
			} catch (NumberFormatException e) {
				throw new Exception("IP地址格式不合法,您的ip地址为" + ip);
			}
		}
		return flag;

	}

	//类型（2）###.###.*.*
	//类型（3）###.###.###.*
	//类型（4）###.###.###.###
	public static boolean isInRangeTwo(String ip, String cidr) throws Exception {

		String startips = cidr.replace("*", "0");
		String endips = cidr.replace("*", "255");
		//String cidrs = startips+"-"+endips;
		StringBuffer cidrs = new StringBuffer();
		cidrs.append(startips);
		cidrs.append("-");
		cidrs.append(endips);
		boolean flag = isInRange(ip, cidrs.toString());
		return flag;
	}

	public static void main(String[] args) throws Exception { // 192.168.1.1/24
		String cidr = "192.168.1.1-192.168.1.3;192.168.1.*;";
		String ip = "192.168.1.108";
		System.out.println(isInRanges(ip, cidr));

	}
}
