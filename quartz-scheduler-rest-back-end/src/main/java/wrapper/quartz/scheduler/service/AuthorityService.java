package wrapper.quartz.scheduler.service;

import org.springframework.stereotype.Service;
import wrapper.quartz.scheduler.entity.jpa.Authority;
import wrapper.quartz.scheduler.enums.AuthorityEnum;
import wrapper.quartz.scheduler.repository.jpa.AuthorityRepositoryService;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Authority service.
 */
@Service
public class AuthorityService {

    private final AuthorityRepositoryService authorityRepositoryService;

    /**
     * Instantiates a new Authority service.
     *
     * @param authorityRepositoryService the authority repository service
     */
    public AuthorityService(AuthorityRepositoryService authorityRepositoryService) {
        this.authorityRepositoryService = authorityRepositoryService;
    }

    /**
     * Create authorities list.
     *
     * @return the list
     * @throws IllegalArgumentException the illegal argument exception
     * @throws PersistenceException     the persistence exception
     */
    public List<Authority> createAuthorities() throws IllegalArgumentException, PersistenceException {
        return AuthorityEnum.getAuthorities()
                .stream()
                .map((AuthorityEnum it) -> {
                    Authority authority = authorityRepositoryService.findByNameEqualsAndDeleted(it, false);
                    if (authority == null) {
                        authority = new Authority();
                        authority.setName(it);
                        authority = authorityRepositoryService.save(authority);
                    }
                    return authority;
                })
                .collect(Collectors.toList());
    }
}
