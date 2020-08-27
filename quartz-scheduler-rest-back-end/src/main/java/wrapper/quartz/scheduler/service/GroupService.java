package wrapper.quartz.scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wrapper.quartz.scheduler.entity.jpa.QuartzGroup;
import wrapper.quartz.scheduler.repository.jpa.QuartzGroupRepositoryService;

import javax.persistence.PersistenceException;

/**
 * The type Group service.
 */
@Service
@Slf4j
public class GroupService {
    private final QuartzGroupRepositoryService quartzGroupRepositoryService;

    /**
     * Instantiates a new Group service.
     *
     * @param quartzGroupRepositoryService the group repository service
     */
    public GroupService(QuartzGroupRepositoryService quartzGroupRepositoryService) {
        this.quartzGroupRepositoryService = quartzGroupRepositoryService;
    }

    /**
     * Create admin group group.
     *
     * @return the group
     * @throws IllegalArgumentException the illegal argument exception
     * @throws PersistenceException     the persistence exception
     */
    public QuartzGroup createAdminGroup() throws IllegalArgumentException, PersistenceException {
        QuartzGroup quartzGroup = quartzGroupRepositoryService.findByNameEqualsAndDeleted("Admin", false);
        if (quartzGroup == null) {
            quartzGroup = new QuartzGroup();
            quartzGroup.setName("Admin");
            quartzGroup = quartzGroupRepositoryService.save(quartzGroup);
        }
        return quartzGroup;
    }
}