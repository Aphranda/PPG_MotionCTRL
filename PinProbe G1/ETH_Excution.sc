
gRdData = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}

// 服务端网络连接
function eth_connect(netIdx, gProtoType) 
	gRtn = net.NetClose(netIdx)
	delay(10)
	gRtn = net.NetOpen(netIdx,5000,"192.168.1.110",10,gProtoType)
	print("***************ETH CONNECT***********************")
	if gRtn ~= 0 then
		print("net open error")
		print(gRtn)
	end
	gInitFlag = 1
	gNetInitedFlag = 0
end

// 服务端网络断开
function eth_disconnect(netIdx, gProtoType) 
	if gNetInitedFlag > 0 then
		print("***************ETH DISCONNECT***********************")
		gRtn = net.NetClose(netIdx)
		delay(10)
		gRtn = net.NetOpen(netIdx,5000,"192.168.1.110",10,gProtoType)
		if gRtn ~= 0 then
			print("net open error")
			print(gRtn)
		end
		gNetInitedFlag = 0
	end
end

// 客户端网络连接

// 读取数据
function eth_read(netIdx) 
	local recBuf = ""
	local gRtn = 0				// 返回值
	local gRLen = 0				// 读取的数据长度

	if net.NetSts(netIdx) > 0 then
		gNetInitedFlag = 1
		gRLen,gRtn = net.NetRead(netIdx,gRdData,32)
		if gRLen > 0 then
			recBuf = ""
			for i=1,gRLen,1 do 
				recBuf = recBuf..string.char(gRdData[i])
			end
			print(recBuf)
		end
		return recBuf
	end
end

// 写入数据
function eth_write(netIdx, scpi_recv) 
	local gRLen = 0
	if net.NetSts(netIdx) > 0 then
		gNetInitedFlag = 1
		gRLen, gRtn = net.netwritestr(netIdx,scpi_recv)
		delay(5)
	end
end

// 修改IP
function eth_modify(ip) 
	local dot_array = {1,1,1,1}
	local ip_array = {"0","0","0","0"}
	local array_index = 1
	local dot_index = 0
	
	ip_len = string.len(ip) -2
	for i=1,ip_len,1 do 
		ip_ascii = string.byte(ip,i)
		if ip_ascii == 46 then 
		dot_array[array_index] = i
		array_index = array_index +1
		end
	end
	ip_array[1] = string.sub(ip,1,dot_array[1]-1)
	ip_array[2] = string.sub(ip,dot_array[1]+1,dot_array[2]-1)
	ip_array[3] = string.sub(ip,dot_array[2]+1,dot_array[3]-1)
	ip_array[4] = string.sub(ip,dot_array[3]+1,ip_len)
	for i=1,4,1 do 
		ip_num_len = string.len(ip_array[i])
		if ip_num_len >3 then 
			return "ETH ERROR"
		end
	end
	
	str_ip = string.format("%s.%s.%s.%s",ip_array[1],ip_array[2],ip_array[3],ip_array[4])
	str_dsn = string.format("%s.%s.%s.%s",ip_array[1],ip_array[2],ip_array[3],"1")
	
	mask_flag = ToNumber(ip_array[1])
	if mask_flag < 128 then 
		str_mask = "255.0.0.0"
	elseif mask_flag<192 and mask_flag>=128 then
		str_mask = "255.255.0.0"
	elseif mask_flag<224 and mask_flag>=192 then
		str_mask = "255.255.255.0"
    end
	eth_write(0,string.format("ETH OK %s", str_ip))
	delay(10)
	net.netsetip(1,str_ip,str_dsn,str_mask)
	return "ETH OK"
end
