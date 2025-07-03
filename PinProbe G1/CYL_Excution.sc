// 验证压紧气缸和电机的功能
function cyl_excution(param1)
	if cyl_test == 1 then 
		print(" =CYL= ")
		cyl1_encode(1)
		delay(1000)
		cyl1_encode(0)
		delay(1000)
		cyl1_encode(1)
		delay(1000)
		cyl1_encode(0)
		delay(1000)
		cyl2_encode(1)
		delay(1000)
		cyl2_encode(0)
	end
end

// 开关门控制
function cyl1_encode(value) 
	// 气缸DOWN
	if value == 1 then
		mc.setdo(7,1)
		mc.setdo(8,0)
	end
	// 气缸UP
	if value == 2 then 
		mc.setdo(7,0)
		mc.setdo(8,1)
    end
	// 气缸STOP
	if value == 0 then 
		mc.setdo(7,0)
		mc.setdo(8,0)
    end
	
end

function cyl1_decode(param1) 
	upstate = mc.getdi(2)
	downstate = mc.getdi(3)
	print("UP:", upstate,"DOWN:",downstate)
	if upstate == 0 and downstate == 1 then 
		return "UP"
    end
	if downstate == 0 and upstate == 1then 
		return "DOWN"
    end
	
	if upstate + downstate == 2 then
		return "MID"
	end

	return "CYL ERROR"
end


