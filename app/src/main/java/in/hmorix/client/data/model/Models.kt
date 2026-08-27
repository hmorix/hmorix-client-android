package in.hmorix.client.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("data") val data: T? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("message") val message: String? = null
)

data class User(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String = "client",
    @SerializedName("company") val company: String? = null,
    @SerializedName("avatar") val avatar: String? = null
)

data class AuthResponse(
    @SerializedName("user") val user: User,
    @SerializedName("token") val token: String? = null,
    @SerializedName("sessionId") val sessionId: String? = null
)

data class PortalStats(
    @SerializedName("activeProjects") val activeProjects: Int = 0,
    @SerializedName("openTickets") val openTickets: Int = 0,
    @SerializedName("invoicesDue") val invoicesDue: Double = 0.0,
    @SerializedName("teamMembers") val teamMembers: Int = 0
)

data class Project(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("status") val status: String = "in_progress",
    @SerializedName("progress") val progress: Int = 0,
    @SerializedName("updatedAt") val updatedAt: String? = null
)

data class Ticket(
    @SerializedName("_id") val id: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("description") val description: String,
    @SerializedName("status") val status: String = "open",
    @SerializedName("priority") val priority: String = "medium",
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("replies") val replies: List<TicketReply>? = null
)

data class TicketReply(
    @SerializedName("author") val author: String,
    @SerializedName("message") val message: String,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("isStaff") val isStaff: Boolean = false
)

data class CreateTicketRequest(
    @SerializedName("projectId") val projectId: String = "",
    @SerializedName("subject") val subject: String,
    @SerializedName("description") val description: String,
    @SerializedName("priority") val priority: String = "medium"
)

data class Invoice(
    @SerializedName("_id") val id: String,
    @SerializedName("invoiceNumber") val invoiceNumber: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("status") val status: String = "unpaid",
    @SerializedName("dueDate") val dueDate: String? = null,
    @SerializedName("pdfUrl") val pdfUrl: String? = null
)

data class TeamMember(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("role") val role: String,
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("email") val email: String? = null
)

data class ActivityItem(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("timestamp") val timestamp: String
)

data class PortalData(
    @SerializedName("user") val user: User?,
    @SerializedName("stats") val stats: PortalStats = PortalStats(),
    @SerializedName("projects") val projects: List<Project> = emptyList(),
    @SerializedName("tickets") val tickets: List<Ticket> = emptyList(),
    @SerializedName("invoices") val invoices: List<Invoice> = emptyList(),
    @SerializedName("teams") val teams: List<TeamMember> = emptyList(),
    @SerializedName("activities") val activities: List<ActivityItem> = emptyList()
)

data class AIChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "user" or "ai"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
