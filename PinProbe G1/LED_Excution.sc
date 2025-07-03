// 验证LED功能
function led_excution(param1) 
	// 验证IO执行功能-高阻态/低电平
	if led_test ==1 then 
		print(" =LED= ")
		delay(1000)
		led_encode(1)
		delay(1000)
		led_encode(2)
		delay(1000)
		led_encode(3)
		delay(1000)
		led_encode(0)
    end
end

	// LED灯切换实现，红：1，绿：2，黄：3.
function led_encode(index)
	if index == 1 then
		mc.setdo(10,1)
		mc.setdo(11,0)
		mc.setdo(12,0)
		return "RED OK"
	elseif index == 2 then
		mc.setdo(10,0)
		mc.setdo(11,1)
		mc.setdo(12,0)
		return "GREEN OK"
	elseif index == 3 then
		mc.setdo(10,0)
		mc.setdo(11,0)
		mc.setdo(12,1)
		return "YELLOE OK"
	elseif index == 0 then
		mc.setdo(10,0)
		mc.setdo(11,0)
		mc.setdo(12,0)
		return "OFF OK"
	end
	return "LED ERROR"
end
	// LED灯查询实现，红：1，绿：2，黄：3.
function led_decode(index) 
	red = mc.getdo(10)
	green = mc.getdo(11)
	yellow = mc.getdo(12)
	
	all = red + green + yellow
	if red == 1 then 
		return "RED"
    end
	
	if green == 1 then 
		return "GREEN"
    end
	
	if yellow == 1 then
		return "YELLOW"
	end
	
	if all == 0 then
		return "OFF"
	end
	return "LED ERROR"
end

function led_Redflash() 
	led_encode(1)
	delay(250)
	led_encode(0)
	delay(250)
	led_encode(1)
	delay(250)
end
