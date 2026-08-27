package in.hmorix.client.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import in.hmorix.client.R
import in.hmorix.client.data.repository.PortalRepository
import in.hmorix.client.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    repository: PortalRepository,
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Hexagonal Logo Icon
            Image(
                painter = painterResource(id = R.drawable.ic_logo_hex),
                contentDescription = "HMorix Logo",
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "HMorix Client Portal",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Cream
            )

            Text(
                text = "Sign in to manage projects, invoices & tickets",
                fontSize = 13.sp,
                color = CreamMuted,
                modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
            )

            if (errorMessage != null) {
                Surface(
                    color = AccentRed.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentRed.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = AccentRed,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                label = { Text("Email Address", color = CreamMuted) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = ElectricLime) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricLime,
                    unfocusedBorderColor = ObsidianBorder,
                    focusedTextColor = Cream,
                    unfocusedTextColor = Cream,
                    focusedContainerColor = ObsidianElevated,
                    unfocusedContainerColor = ObsidianElevated
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text("Password", color = CreamMuted) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ElectricLime) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = CreamMuted
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricLime,
                    unfocusedBorderColor = ObsidianBorder,
                    focusedTextColor = Cream,
                    unfocusedTextColor = Cream,
                    focusedContainerColor = ObsidianElevated,
                    unfocusedContainerColor = ObsidianElevated
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sign In Button
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Please enter both email and password"
                        return@Button
                    }
                    loading = true
                    errorMessage = null
                    scope.launch {
                        val result = repository.signIn(email.trim(), password.trim())
                        loading = false
                        result.onSuccess {
                            onLoginSuccess()
                        }.onFailure {
                            errorMessage = it.message ?: "Authentication failed"
                        }
                    }
                },
                enabled = !loading,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricLime, contentColor = ObsidianBg),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = ObsidianBg, strokeWidth = 2.dp)
                } else {
                    Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sign Up link
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don't have an account? ", color = CreamMuted, fontSize = 13.sp)
                Text(
                    text = "Sign Up",
                    color = ElectricLime,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToSignUp() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    repository: PortalRepository,
    onSignUpSuccess: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_hex),
                contentDescription = "HMorix Logo",
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text("Create Client Account", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Cream)
            Text("Join HMorix enterprise ecosystem", fontSize = 13.sp, color = CreamMuted, modifier = Modifier.padding(top = 4.dp, bottom = 24.dp))

            if (errorMessage != null) {
                Surface(
                    color = AccentRed.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentRed.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Text(errorMessage ?: "", color = AccentRed, fontSize = 12.sp, modifier = Modifier.padding(12.dp))
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it; errorMessage = null },
                label = { Text("Full Name", color = CreamMuted) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ElectricLime) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricLime,
                    unfocusedBorderColor = ObsidianBorder,
                    focusedTextColor = Cream,
                    unfocusedTextColor = Cream,
                    focusedContainerColor = ObsidianElevated,
                    unfocusedContainerColor = ObsidianElevated
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                label = { Text("Work Email", color = CreamMuted) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = ElectricLime) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricLime,
                    unfocusedBorderColor = ObsidianBorder,
                    focusedTextColor = Cream,
                    unfocusedTextColor = Cream,
                    focusedContainerColor = ObsidianElevated,
                    unfocusedContainerColor = ObsidianElevated
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text("Password", color = CreamMuted) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ElectricLime) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricLime,
                    unfocusedBorderColor = ObsidianBorder,
                    focusedTextColor = Cream,
                    unfocusedTextColor = Cream,
                    focusedContainerColor = ObsidianElevated,
                    unfocusedContainerColor = ObsidianElevated
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isBlank() || email.isBlank() || password.isBlank()) {
                        errorMessage = "All fields are required"
                        return@Button
                    }
                    loading = true
                    errorMessage = null
                    scope.launch {
                        val result = repository.signUp(name.trim(), email.trim(), password.trim())
                        loading = false
                        result.onSuccess {
                            onSignUpSuccess()
                        }.onFailure {
                            errorMessage = it.message ?: "Sign up failed"
                        }
                    }
                },
                enabled = !loading,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricLime, contentColor = ObsidianBg),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = ObsidianBg, strokeWidth = 2.dp)
                } else {
                    Text("Create Account", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Already have an account? ", color = CreamMuted, fontSize = 13.sp)
                Text("Sign In", color = ElectricLime, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onNavigateToSignIn() })
            }
        }
    }
}
