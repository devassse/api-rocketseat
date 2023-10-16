package mz.co.keomasoftware.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){
        var userId = request.getAttribute("userId");
        taskModel.setUserId((UUID) userId);

        var currentDate = LocalDateTime.now();
        if(currentDate.isAfter(taskModel.getStartDate()) || currentDate.isAfter(taskModel.getEndDate())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datas devem ser maiores que Atual");
        }

         if(taskModel.getStartDate().isAfter(taskModel.getEndDate())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data de inicio deve ser maior que data de fim");
        }

        TaskModel task =  this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/tasks")
    public List<TaskModel> list(HttpServletRequest request){
        var userId = request.getAttribute("userId");
        var tasks =  this.taskRepository.findByUserId((UUID) userId);
        return tasks;
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID taskId){
        var task = this.taskRepository.findById(taskId);
        var userId = request.getAttribute("userId");
        
        System.out.println("Task" + task);

        //Validate Tasks Ownaship
        // if(!task.getUserId().equals(userId)){
        //     ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task nao pertence a esse Utilizador");
        // }

        taskModel.setUserId((UUID) userId);
        taskModel.setTaskId(taskId);
        // var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(this.taskRepository.save(taskModel));
    }
    
}
