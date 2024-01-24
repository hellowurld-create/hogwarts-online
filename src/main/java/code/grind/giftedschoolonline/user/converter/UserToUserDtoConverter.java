package code.grind.giftedschoolonline.user.converter;

import code.grind.giftedschoolonline.user.User;
import code.grind.giftedschoolonline.user.dto.UserDto;
import code.grind.giftedschoolonline.wizard.Wizard;
import code.grind.giftedschoolonline.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

    @Component
    public class UserToUserDtoConverter implements Converter<User, UserDto> {

        @Override
        public UserDto convert(User source) {
           final UserDto userDto = new UserDto(source.getId(),
                    source.getUsername(),
                    source.getEnabled(),
                    source.getRoles());
            return userDto;
        }
    }
