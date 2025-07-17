gHomingStartFlag = 0 // 启动原点复位标志

// 验证电机功能
function motion_excution(param1) 
	if motion_test == 1 then 
		print(" =MOTION= ")
		rtn = motion_abs(100,100,100,100)
		print(rtn)
		rtn = motion_abs(0,100,100,100)
		print(rtn)
	end
end


// 电机执行运动mm
function motion_encode(value)
	pos = value
	// pos = axis_equivalent / axis_guide * value
	return pos
end

// 回零操作，并判断回零是否完成
function motion_home() 
	local runFlag = 1
	rtn1 = scpi_cyl_excution("CONF:CYL1 UP") 		// 气缸上升
	if rtn1 == "UP" then
		while runFlag > 0 do 
			if gHomingStartFlag == 0 then 
				print("Start Homing")
				mc.movrel(axis,50,0.5,100,100)
				delay(500)
				gHomingStartFlag = 1 // 回零标志位复位
				rtn = mc.axissts(1,0)
				print("==[AxisSts][Busy:",rtn,"]==")
				rtn = mc.homeaxis(1)
				print("==[AxisHome]", rtn, "==")
            end
			if mc.ishomeaxisok(1) > 0 then 
				print("Homing OK")
				mc.movstop(1,0)
				delay(100)
				gHomingStartFlag = 0
				runFlag = 0
				led_encode(0)
            end
        end
		motion_abs(-60,50,100,100) // 偏置
		gTestStep = 0
		gSMStep = 0
		ext_ResetOK(500) // 复位完成，给外部脉冲信号
	end
end


function motion_home1()
	rtn = mc.homeaxis(1)
	print("======HOME=====",rtn)
	rtn = mc.ishomeaxisok(1)
	print("======HOME=====",rtn)
	count = 0
	countMax = 100
	while count < countMax do 
		rtn = mc.axissts(axis,0)
		if rtn == 0 then 
			return "MOTION COMPLETE"
        end
		count = count + 1
		delay(100)
    end
	return"MOTION ERROR"
end

// 单轴运行 pos-mm,vel-mm/s,acc&dec mm^2/s
function motion_abs(pos, vel, acc, dec)
	pos = motion_encode(pos)
	vel = motion_encode(vel)
	acc = motion_encode(acc)
	dec = motion_encode(dec)
	rtn = mc.movabs(axis,vel,pos,acc,dec)
	print("======POS=====",pos, vel)
	count = 0
	countMax = pos/vel+30
	while count < countMax do 
		rtn = mc.axissts(axis,0)
		if rtn == 0 then 
			return "MOTION COMPLETE"
        end
		count = count + 1
		delay(100)
    end
	return"MOTION ERROR"
end


function motion_decode(param1) 
	rtn = mc.axissts(axis,0)
	if rtn == 0 then 
		pos = mc.getpos(1,1)
		print("+++",pos,axis_aim_pos)
		if pos == motion_encode(axis_aim_pos) then
			return "CLOSE OK"
		end
		if pos == motion_encode(axis_zero_pos) then
			return "OPEN OK"
		end
		return "SERVO ERROR"
	else
		return "RUNING"
    end
end

function motion_close(param1) 
	rtn = motion_abs(axis_aim_pos,axis_aim_vel,axis_aim_acc,axis_aim_dec)
	return rtn
end

function motion_open(param1) 
	rtn = motion_abs(axis_zero_pos,axis_aim_vel,axis_aim_acc,axis_aim_dec)
	return rtn
end

function check_zero(param1) 
	pos_Zero = mc.getpos(1,0)
	if (pos_Zero +60)<0.1 then 
		mc.setdo(9,1)
    end
	if (pos_Zero +60) >0.2 then 
		mc.setdo(9,0)
    end
end




