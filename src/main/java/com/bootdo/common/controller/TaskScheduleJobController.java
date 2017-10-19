package com.bootdo.common.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bootdo.common.domain.TaskScheduleJobDO;
import com.bootdo.common.service.TaskScheduleJobService;
import com.bootdo.common.utils.PageUtils;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.R;

/**
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-26 20:53:48
 */
@Controller
@RequestMapping("/common/taskScheduleJob")
public class TaskScheduleJobController extends BaseController{
	@Autowired
	private TaskScheduleJobService taskScheduleJobService;

	@GetMapping()
	// @RequiresPermissions("common:taskScheduleJob")
	String TaskScheduleJob() {
		return "common/taskScheduleJob/taskScheduleJob";
	}

	@ResponseBody
	@GetMapping("/list")
	// @RequiresPermissions("common:list")
	public PageUtils list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<TaskScheduleJobDO> taskScheduleJobList = taskScheduleJobService.list(query);
		int total = taskScheduleJobService.count(query);
		PageUtils pageUtils = new PageUtils(taskScheduleJobList, total);
		return pageUtils;
	}

	@GetMapping("/add")
	// @RequiresPermissions("blog:bComments")
	String add() {
		return "common/taskScheduleJob/add";
	}

	@GetMapping("/edit/{id}")
	// @RequiresPermissions("blog:bComments")
	String edit(@PathVariable("id") Long id, Model model) {
		TaskScheduleJobDO taskScheduleJob = taskScheduleJobService.get(id);
		model.addAttribute("TaskScheduleJob", taskScheduleJob);
		return "common/taskScheduleJob/edit";
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	// @RequiresPermissions("common:info")
	public R info(@PathVariable("id") Long id) {
		TaskScheduleJobDO taskScheduleJob = taskScheduleJobService.get(id);
		return R.ok().put("taskScheduleJob", taskScheduleJob);
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	// @RequiresPermissions("common:save")
	public R save(TaskScheduleJobDO taskScheduleJob) {
		if ("test".equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		if (taskScheduleJobService.save(taskScheduleJob) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	// @RequiresPermissions("common:update")
	public R update(@RequestBody TaskScheduleJobDO taskScheduleJob) {
		if ("test".equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		taskScheduleJobService.update(taskScheduleJob);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	// @RequiresPermissions("common:remove")
	public R remove(Long id) {
		if ("test".equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		if (taskScheduleJobService.remove(id) > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	// @RequiresPermissions("common:remove")
	public R remove(@RequestParam("ids[]") Long[] ids) {
		if ("test".equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		taskScheduleJobService.batchRemove(ids);

		return R.ok();
	}

	@PostMapping(value = "/changeJobStatus")
	@ResponseBody
	public R changeJobStatus(Long id,String cmd ) {
		if ("test".equals(getUsername())) {
			return R.error(1, "演示系统不允许修改,完整体验请部署程序");
		}
		String label = "停止";
		if (cmd.equals("start")) {
			label = "启动";
		} else {
			label = "停止";
		}
		try {
			taskScheduleJobService.changeStatus(id, cmd);
			return R.ok("任务" + label + "成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return R.ok("任务" + label + "失败");
	}

}
