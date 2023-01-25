package data.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto implements UserDetails {
	
	private int user_num;
	private String user_name;
	
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) 
	// Write할때만 접근 허용. 응답 결과를 생성할 때는 해당 필드는 제외된다
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String user_pass;
	private String user_email;
	private String user_id;
	private String auth;
	private int state;
	
	@Override
	public String toString() {
		return "UserDto [user_id=" + user_id + ", user_pass=" + user_pass + 
				", user_name=" + user_name + ", auth=" + auth + ", state="
				+ state + "]";
	}
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    
    @Override
    public String getPassword() {
        return this.user_pass;
    }

    @Override
    public String getUsername() {
        return this.user_id;
    }
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

}
