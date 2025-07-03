// 验证IO执行功能-高阻态/低电平
function jog_excution(param1) 
	if rf_test == 1 then 
		print(" =RF= ")
		delay(3000)
		switch1_encode(1)
		switch1_encode(2)
		switch1_encode(4)
    end
end

// 开关一切换功能实现
function switch1_encode(index)
	if index == 1 then
		mc.setdo(1,0)
		mc.setdo(2,0)
		mc.setdo(3,0)
	elseif index == 2 then
		mc.setdo(1,1)
		mc.setdo(2,0)
		mc.setdo(3,0)	
	elseif index == 3 then
		mc.setdo(1,0)
		mc.setdo(2,1)
		mc.setdo(3,0)	
	elseif index == 4 then
		mc.setdo(1,1)
		mc.setdo(2,1)
		mc.setdo(3,0)	
	elseif index == 5 then
		mc.setdo(1,0)
		mc.setdo(2,0)
		mc.setdo(3,1)	
	elseif index == 6 then
		mc.setdo(1,1)
		mc.setdo(2,0)
		mc.setdo(3,1)	
	elseif index == 7 then
		mc.setdo(1,0)
		mc.setdo(2,1)
		mc.setdo(3,1)	
	elseif index == 8 then
		mc.setdo(1,1)
		mc.setdo(2,1)
		mc.setdo(3,1)	
	end
end

// 开关二切换功能实现
function switch2_encode(index)
	if index == 1 then
		mc.setdo(4,0)
		mc.setdo(5,0)
		mc.setdo(6,0)
	elseif index == 2 then
		mc.setdo(4,1)
		mc.setdo(5,0)
		mc.setdo(6,0)	
	elseif index == 3 then
		mc.setdo(4,0)
		mc.setdo(5,1)
		mc.setdo(6,0)	
	elseif index == 4 then
		mc.setdo(4,1)
		mc.setdo(5,1)
		mc.setdo(6,0)	
	elseif index == 5 then
		mc.setdo(4,0)
		mc.setdo(5,0)
		mc.setdo(6,1)	
	elseif index == 6 then
		mc.setdo(4,1)
		mc.setdo(5,0)
		mc.setdo(6,1)	
	elseif index == 7 then
		mc.setdo(4,0)
		mc.setdo(5,1)
		mc.setdo(6,1)	
	elseif index == 8 then
		mc.setdo(4,1)
		mc.setdo(5,1)
		mc.setdo(6,1)	
	end
end

// 开关1 IO解码
function switch1_decode(param1) 
	one = mc.getdo(1)
	two = mc.getdo(2)
	three = mc.getdo(3)
	
	index = one + two*2^1 +three * 2^2 + 1
	return index
end

// 开关2 IO解码
function switch2_decode(param1) 
	one = mc.getdo(4)
	two = mc.getdo(5)
	three = mc.getdo(6)
	
	index = one + two*2^1 +three * 2^2 + 1
	return index
end

